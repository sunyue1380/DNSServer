package cn.schoolwow.dns;

import cn.schoolwow.dns.domain.DNSRequest;
import cn.schoolwow.dns.domain.DNSResponse;
import cn.schoolwow.dns.domain.header.Header;
import cn.schoolwow.dns.domain.header.constant.AA;
import cn.schoolwow.dns.domain.question.Question;
import cn.schoolwow.dns.domain.question.constants.QTYPE;
import cn.schoolwow.dns.domain.rr.ResourceRecord;
import org.junit.Test;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DNSRequestResponseTest {
    byte[] bytes = new byte[]{0,35,1,0,0,1,0,0,0,0,0,0,5,98,97,105,100,117,3,99,111,109,3,99,111,109,0,0,1,0,1};

    @Test
    public void testDNSRequest() throws IOException {
        DNSRequest dnsRequest = new DNSRequest(bytes);
        System.out.println(dnsRequest.toString());
    }

    @Test
    public void testReadDNSResponse() throws IOException {
        Path path = Paths.get(System.getProperty("user.dir")+"/src/test/resources/response_domain.bin");
        byte[] bytes = Files.readAllBytes(path);
        DNSResponse dnsResponse = new DNSResponse(bytes);
        System.out.println(dnsResponse);
    }

    @Test
    public void testDNSResponse() throws IOException {
        DNSRequest dnsRequest = new DNSRequest(bytes);
        //设置答案
        ResourceRecord answer = dnsRequest.getQuestions()[0].answer();
        answer.setTTL(600);
        answer.setRDATA(QTYPE.A,"127.0.0.1");
        //DNS响应
        DNSResponse dnsResponse = dnsRequest.getDNSResponse();
        dnsResponse.answer(answer)
                .aa(AA.AUTHORITATIVE_ANSWER);
        System.out.println(dnsResponse);
    }

    @Test
    public void testA() throws IOException {
        Question question = new Question();
        question.setQNAME("baidu.com");
        question.setQTYPE(QTYPE.A);
        forwardDNSRequest(question);
    }

    @Test
    public void testNS() throws IOException {
        Question question = new Question();
        question.setQNAME("baidu.com");
        question.setQTYPE(QTYPE.NS);
        forwardDNSRequest(question);
    }

    @Test
    public void testMX() throws IOException {
        Question question = new Question();
        question.setQNAME("baidu.com");
        question.setQTYPE(QTYPE.MX);
        forwardDNSRequest(question);
    }

    @Test
    public void testCNAME() throws IOException {
        Question question = new Question();
        question.setQNAME("baidu.com");
        question.setQTYPE(QTYPE.CNAME);
        forwardDNSRequest(question);
    }

    private void forwardDNSRequest(Question question) throws IOException {
        String ip = "223.5.5.5";
        Header header = new Header();
        header.setID((int) Math.round(Math.random()*65535));
        header.setQuestionCount(1);

        DNSRequest dnsRequest = new DNSRequest();
        dnsRequest.setHeader(header);
        dnsRequest.setQuestions(new Question[]{question});

        DatagramSocket socket = new DatagramSocket();
        byte[] data = dnsRequest.toByteArray();
        DatagramPacket packet = new DatagramPacket(data, data.length);
        socket.connect(new InetSocketAddress(ip,53));
        socket.send(packet);
        data = new byte[512];
        packet.setData(data);
        socket.receive(packet);
        System.out.println(packet.getAddress().getHostAddress());
        byte[] trimData = new byte[packet.getLength()];
        System.arraycopy(data,0,trimData,0,trimData.length);
        DNSResponse dnsResponse = new DNSResponse(trimData);
        System.out.println(dnsResponse);
        socket.close();
    }
}