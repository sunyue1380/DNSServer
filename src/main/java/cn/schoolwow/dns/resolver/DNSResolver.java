package cn.schoolwow.dns.resolver;

import cn.schoolwow.dns.constants.ResourceRecordType;
import cn.schoolwow.dns.domain.DNSRequest;
import cn.schoolwow.dns.domain.DNSResponse;
import cn.schoolwow.dns.domain.header.Header;
import cn.schoolwow.dns.domain.header.constant.AA;
import cn.schoolwow.dns.domain.header.constant.RA;
import cn.schoolwow.dns.domain.header.constant.RCODE;
import cn.schoolwow.dns.domain.question.Question;
import cn.schoolwow.dns.domain.question.constants.QTYPE;
import cn.schoolwow.dns.domain.rr.ResourceRecord;
import cn.schoolwow.dns.domain.rr.ResourceRecordImpl;
import cn.schoolwow.dns.entity.*;
import cn.schoolwow.dns.entity.ignore.DNSHeaderQuestionRecord;
import cn.schoolwow.quickdao.dao.DAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

/**DNS处理线程*/
@Component
public class DNSResolver implements InitializingBean {
    private Logger logger = LoggerFactory.getLogger(DNSResolver.class);

    @Resource
    private ThreadPoolExecutor dnsResolverThreadPool;

    @Resource
    private DAO dao;

    private DatagramSocket requestDatagramSocket = new DatagramSocket(53);

    public DNSResolver() throws SocketException {
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        logger.info("[监听udp协议53端口]");
        listenDNS();
    }

    /**
     * 监听udp53端口
     * */
    private void listenDNS(){
        new Thread(()->{
            try {
                requestDatagramSocket.setSoTimeout(0);
                while(true){
                    byte[] data = new byte[512];
                    DatagramPacket datagramPacket = new DatagramPacket(data, data.length);
                    requestDatagramSocket.receive(datagramPacket);
                    //如果报文长度小于12字节,则忽略该报文
                    if(datagramPacket.getLength()<12){
                        logger.warn("[忽略报文]该报文长度小于12字节!当前报文长度:"+datagramPacket.getLength());
                        continue;
                    }
                    //新建子线程执行处理过程
                    dnsResolverThreadPool.execute(()->{
                        try {
                            dispatch(datagramPacket);
                        } catch (IOException e) {
                            e.printStackTrace();
                            //插入错误报文记录
                            try {
                                DNSErrorDatagram dnsErrorDatagram = new DNSErrorDatagram();
                                Blob blob = new SerialBlob(datagramPacket.getData());
                                dnsErrorDatagram.setBlob(blob);
                                dnsErrorDatagram.setNormal(false);
                                dnsErrorDatagram.setMessage(e.getMessage());
                                dao.insert(dnsErrorDatagram);
                            } catch (SQLException ex) {
                                ex.printStackTrace();
                            }
                        }
                    });
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * 请求分发处理
     * */
    private void dispatch(DatagramPacket datagramPacket) throws IOException {
        DNSRequest dnsRequest = getDNSRequest(datagramPacket);
        DNSResponse dnsResponse = null;
        if(dnsRequest.getQuestions().length==1){
            DNSRequestRecord dnsRequestRecord = addDNSRequestRecord(datagramPacket,dnsRequest);
            dnsResponse = handleSingleQuestion(dnsRequest);
            addDNSResponseRecord(dnsRequestRecord,dnsResponse);
        }else{
            dnsResponse = handleMultiQuestion(dnsRequest);
        }
        sendDNSResponse(datagramPacket,dnsResponse);
    }

    /**
     * 获取DNSRequest
     * */
    private DNSRequest getDNSRequest(DatagramPacket datagramPacket) throws IOException {
        byte[] trimData = new byte[datagramPacket.getLength()];
        System.arraycopy(datagramPacket.getData(),0,trimData,0,datagramPacket.getLength());
        datagramPacket.setData(trimData);
        DNSRequest dnsRequest = new DNSRequest(trimData);
        return dnsRequest;
    }

    /**
     * 插入DNSRequestRecord记录
     * */
    private DNSRequestRecord addDNSRequestRecord(DatagramPacket datagramPacket, DNSRequest dnsRequest) throws IOException {
        DNSRequestRecord dnsRequestRecord = new DNSRequestRecord();
        dnsRequestRecord.setIp(datagramPacket.getAddress().getHostAddress());
        dnsRequestRecord.setPort(datagramPacket.getPort());
        Header header = dnsRequest.getHeader();
        Question question = dnsRequest.getQuestions()[0];
        setDNSHeaderQuestionRecord(dnsRequestRecord,header,question);
        dao.insertIgnore(dnsRequestRecord);
        return dnsRequestRecord;
    }

    /**处理问题只有一个的情况*/
    private DNSResponse handleSingleQuestion(DNSRequest dnsRequest) throws IOException {
        DNSResponse dnsResponse = dnsRequest.getDNSResponse();
        Question question = dnsRequest.getQuestions()[0];
        //判断数据库中是否有匹配记录
        DNSRecord dnsRecord = dao.fetch(DNSRecord.class,"domain",question.getQNAME());
        if(null!=dnsRecord){
            ResourceRecord answer = question.answer();
            answer.setTTL(dnsRecord.getTtl());
            answer.setRDATA(QTYPE.A,dnsRecord.getValue());
            dnsResponse.answer(answer);
            dnsResponse.aa(AA.AUTHORITATIVE_ANSWER);
            return dnsResponse;
        }
        //获取递归查询服务器列表
        DNSRecursionServer recursionQueryRecord = dao.query(DNSRecursionServer.class)
                .addQuery("enable",true)
                .orderBy("order")
                .limit(0,1)
                .execute()
                .getOne();
        if(null==recursionQueryRecord){
            dnsResponse.ra(RA.CAN_NOT_RECURSIVE)
                    .rcode(RCODE.SERVER_FAILURE);
            logger.warn("[本地数据库无对应DNS记录且未启用任何DNS递归服务器]");
        }else{
            dnsResponse =  forwardRequest(dnsRequest, recursionQueryRecord.getDnsServerIP());
        }
        return dnsResponse;
    }

    /**
     * 转发到上级DNS服务器
     * @param ip 上级DNS服务ip地址
     * */
    public DNSResponse forwardRequest(DNSRequest dnsRequest, String ip) throws IOException {
        DatagramSocket socket = new DatagramSocket();
        byte[] data = dnsRequest.toByteArray();
        DatagramPacket packet = new DatagramPacket(data, data.length);
        socket.connect(new InetSocketAddress(ip,53));
        socket.send(packet);
        data = new byte[512];
        packet.setData(data);
        socket.receive(packet);
        byte[] trimData = new byte[packet.getLength()];
        System.arraycopy(data,0,trimData,0,trimData.length);
        try{
            DNSResponse dnsResponse = new DNSResponse(trimData);
            return dnsResponse;
        }catch (Exception e){
            //插入错误报文记录
            try {
                DNSErrorDatagram dnsErrorDatagram = new DNSErrorDatagram();
                Blob blob = new SerialBlob(trimData);
                dnsErrorDatagram.setBlob(blob);
                dnsErrorDatagram.setNormal(false);
                dnsErrorDatagram.setMessage(e.getMessage());
                dao.insert(dnsErrorDatagram);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            throw e;
        }finally {
            socket.close();
        }
    }

    /**
     * 插入DNSRequestRecord记录
     * */
    private void addDNSResponseRecord(DNSRequestRecord dnsRequestRecord, DNSResponse dnsResponse) throws IOException {
        List<DNSResponseRecord> dnsResponseRecordList = new ArrayList<>();
        Header header = dnsResponse.getHeader();
        for(ResourceRecordImpl answer:dnsResponse.getAnswers()){
            DNSResponseRecord dnsResponseRecord = new DNSResponseRecord();
            setDNSHeaderQuestionRecord(dnsResponseRecord, header, answer);
            dnsResponseRecord.setDnsRequestRecordId(dnsRequestRecord.getId());
            dnsResponseRecord.setTtl(answer.getTTL());
            dnsResponseRecord.setValue(answer.getRDATAFormat());
            dnsResponseRecord.setResourceRecordType(ResourceRecordType.ANSWER.name());
            dnsResponseRecordList.add(dnsResponseRecord);
        }
        for(ResourceRecordImpl authority:dnsResponse.getAuthorities()){
            DNSResponseRecord dnsResponseRecord = new DNSResponseRecord();
            setDNSHeaderQuestionRecord(dnsResponseRecord, header, authority);
            dnsResponseRecord.setDnsRequestRecordId(dnsRequestRecord.getId());
            dnsResponseRecord.setTtl(authority.getTTL());
            dnsResponseRecord.setValue(authority.getRDATAFormat());
            dnsResponseRecord.setResourceRecordType(ResourceRecordType.AUTHORITY.name());
            dnsResponseRecordList.add(dnsResponseRecord);
        }
        for(ResourceRecordImpl additional:dnsResponse.getAdditionals()){
            DNSResponseRecord dnsResponseRecord = new DNSResponseRecord();
            setDNSHeaderQuestionRecord(dnsResponseRecord, header, additional);
            dnsResponseRecord.setDnsRequestRecordId(dnsRequestRecord.getId());
            dnsResponseRecord.setTtl(additional.getTTL());
            dnsResponseRecord.setValue(additional.getRDATAFormat());
            dnsResponseRecord.setResourceRecordType(ResourceRecordType.ADDITIONAL.name());
            dnsResponseRecordList.add(dnsResponseRecord);
        }
        dao.insertIgnore(dnsResponseRecordList);
    }

    /**处理问题有多个的情况*/
    private DNSResponse handleMultiQuestion(DNSRequest dnsRequest){
        //直接拒绝处理
        DNSResponse dnsResponse = dnsRequest.getDNSResponse();
        return dnsResponse.rcode(RCODE.QUERY_REFUSED);
    }

    /**
     * 发送响应结果到客户端
     * */
    private void sendDNSResponse(DatagramPacket datagramPacket, DNSResponse dnsResponse) throws IOException {
        byte[] responseData = dnsResponse.toByteArray();
        DatagramPacket responseDatagramPacket = new DatagramPacket(responseData, responseData.length, datagramPacket.getAddress(), datagramPacket.getPort());
        requestDatagramSocket.send(responseDatagramPacket);
    }

    private void setDNSHeaderQuestionRecord(DNSHeaderQuestionRecord dnsHeaderQuestionRecord, Header header, Question question){
        dnsHeaderQuestionRecord.setTransactionId(header.getID());
        dnsHeaderQuestionRecord.setQr(header.getQR().name());
        dnsHeaderQuestionRecord.setOpcode(header.getOPCODE().name());
        dnsHeaderQuestionRecord.setAa(header.getAA().value==1);
        dnsHeaderQuestionRecord.setTc(header.getTC().value==1);
        dnsHeaderQuestionRecord.setRd(header.getRD().name());
        dnsHeaderQuestionRecord.setRa(header.getRA().name());
        dnsHeaderQuestionRecord.setRcode(header.getRCODE().name());
        dnsHeaderQuestionRecord.setQname(question.getQNAME());
        dnsHeaderQuestionRecord.setQtype(question.getQTYPE().name());
        dnsHeaderQuestionRecord.setQclass(question.getQCLASS().name());
    }
}
