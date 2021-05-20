package cn.schoolwow.dns.domain.question;

import cn.schoolwow.dns.domain.holder.DNSRequestHolder;
import cn.schoolwow.dns.domain.question.constants.QCLASS;
import cn.schoolwow.dns.domain.question.constants.QTYPE;
import cn.schoolwow.dns.domain.rr.ResourceRecord;
import cn.schoolwow.dns.domain.rr.ResourceRecordImpl;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.StringTokenizer;

/**问题*/
public class Question {
    /**域名*/
    private String QNAME;

    /**问题类型*/
    private QTYPE qtype;

    /**查询协议类*/
    private QCLASS qclass = QCLASS.IN;

    /**所属DNSRequest*/
    protected DNSRequestHolder dnsRequestHolder;

    public Question(){

    }

    public Question(DNSRequestHolder dnsRequestHolder){
        this.dnsRequestHolder = dnsRequestHolder;
    }

    /**获取查询域名*/
    public String getQNAME() {
        return QNAME;
    }

    /**设置查询域名*/
    public void setQNAME(String QNAME){
        this.QNAME = QNAME;
    }

    /**获取查询类型*/
    public QTYPE getQTYPE() {
        return qtype;
    }

    /**设置查询类型*/
    public void setQTYPE(QTYPE qtype) {
        this.qtype = qtype;
    }

    /**获取查询协议类,一般为0x0001表示Internet*/
    public QCLASS getQCLASS(){
        return qclass;
    }

    /**获取该问题的答案*/
    public ResourceRecord answer(){
        ResourceRecordImpl answer = new ResourceRecordImpl(dnsRequestHolder);
        answer.setQNAME(this.getQNAME());
        answer.setQTYPE(this.getQTYPE());
        return answer;
    }

    /**从输入流读取信息*/
    public void read(DataInputStream dataInputStream) throws IOException {
        QNAME = readDomainName(dataInputStream);
        //获取查询类型
        qtype = QTYPE.getQTYPE(dataInputStream.readShort());
        if(dataInputStream.readShort()==1){
            qclass = QCLASS.IN;
        }
    }

    /**从输入流读取信息*/
    public void write(DataOutputStream dataOutputStream) throws IOException {
        StringTokenizer st = new StringTokenizer(QNAME,".");
        while(st.hasMoreTokens()){
            String token = st.nextToken();
            dataOutputStream.writeByte(token.length());
            dataOutputStream.writeBytes(token);
        }
        dataOutputStream.writeByte(0);
        dataOutputStream.writeShort(qtype.value);
        dataOutputStream.writeShort(1);
    }

    @Override
    public String toString() {
        return "\n{\n" +
                "查询域名:" + getQNAME() + "\n"
                + "查询类型:" + getQTYPE().name() + "\n"
                + "查询协议类:" + getQCLASS().name() + "\n"
                + "}\n";
    }

    /**读取域名信息*/
    protected String readDomainName(DataInputStream dataInputStream) throws IOException {
        byte length = dataInputStream.readByte();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        while(length!=0){
            int highBit = length&0xc0;
            //判断最高两位时00还是11
            if(highBit==0xc0){
                //为11时表示指针
                if(length!=-64){
                    throw new IllegalArgumentException("标量表示字节长度超过512!");
                }
                int nextLength = dataInputStream.readUnsignedByte();
                readOffsetDomain(baos,nextLength);
                break;
            }else if(highBit==0x00){
                do {
                    for(int i=0;i<length;i++){
                        baos.write(dataInputStream.readByte());
                    }
                    length = dataInputStream.readByte();
                    if(length!=0){
                        baos.write('.');
                    }
                }while(length>0);
            }else{
                throw new IllegalArgumentException("无法解析!域名长度前2位必须为00或者11!");
            }
        }
        return new String(baos.toByteArray(), StandardCharsets.UTF_8);
    }

    /**读取指针表示的域名*/
    private void readOffsetDomain(ByteArrayOutputStream baos, int offset) throws IOException {
        if(dnsRequestHolder.pointerStringCache.containsKey(offset)){
            baos.write(dnsRequestHolder.pointerStringCache.get(offset).getBytes(StandardCharsets.UTF_8));
            return;
        }
        int initialOffset = offset;
        int stackTraceLength = Thread.currentThread().getStackTrace().length;
        //如果栈帧超过50则说明递归有问题
        if(stackTraceLength>=200){
            throw new IOException("DNS报文解析失败!递归栈帧超过200!");
        }int length = dnsRequestHolder.data[offset];
        do {
            //判断最高两位时00还是11
            if((length&0xc0)==0xc0){
                //为11时表示指针
                if(length!=-64){
                    throw new IllegalArgumentException("标量表示字节长度超过512!");
                }
                int newOffset = dnsRequestHolder.data[++offset];
                if(newOffset<0){
                    newOffset = newOffset&0x7f + 128;
                }
                readOffsetDomain(baos,newOffset);
                break;
            }else{
                //为00时读取指定字节个数
                for(int i=0;i<length;i++){
                    baos.write(dnsRequestHolder.data[++offset]);
                }
            }
            length = dnsRequestHolder.data[++offset];
            if(length!=0){
                baos.write('.');
            }
        }while(length!=0);
        dnsRequestHolder.pointerStringCache.put(initialOffset, new String(baos.toByteArray(), StandardCharsets.UTF_8));
    }
}
