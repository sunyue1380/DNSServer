package cn.schoolwow.dns.domain.rr;

import cn.schoolwow.dns.domain.holder.DNSRequestHolder;
import cn.schoolwow.dns.domain.question.Question;
import cn.schoolwow.dns.domain.question.constants.QTYPE;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.StringTokenizer;

/**资源记录,包括Answer,Authority,Additional*/
public class ResourceRecordImpl extends Question implements ResourceRecord{
    /**资源记录缓存时间(秒)*/
    private int TTL;

    /**实际数据*/
    private byte[] RDATA;

    /**仅用于格式化数据*/
    private String RDATAFormat;

    public ResourceRecordImpl(DNSRequestHolder dnsRequestHolder) {
        super(dnsRequestHolder);
    }

    /**获取资源缓存时间*/
    public int getTTL() {
        return TTL;
    }

    /**设置资源缓存时间(秒)*/
    @Override
    public void setTTL(int TTL) {
        this.TTL = TTL;
    }

    @Override
    public String getRDATAFormat() {
        return RDATAFormat;
    }

    @Override
    public void setRDATA(QTYPE qtype, String value) throws IOException {
        switch (qtype){
            case A:{
                if(!value.matches("\\d+\\.\\d+\\.\\d+\\.\\d+")){
                    throw new IllegalArgumentException("ipv4地址不合法!ipv4:"+value);
                }
                StringTokenizer st = new StringTokenizer(value,".");
                int index = 0;
                byte[] bytes = new byte[4];
                while(st.hasMoreTokens()){
                    int number = Integer.parseInt(st.nextToken());
                    if(number<0||number>255){
                        throw new IllegalArgumentException("ipv4地址不合法!ipv4:"+value);
                    }
                    bytes[index] = (byte) number;
                    index++;
                }
                RDATA = bytes;
                RDATAFormat = value;
            }break;
            case MX:{
                //一个16位数字,表示优先级,随后是一个domainName
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                DataOutputStream dos = new DataOutputStream(baos);
                dos.writeShort(10);
                dos.writeByte(value.length());
                dos.writeBytes(value);
                RDATA = baos.toByteArray();
                RDATAFormat = value;
            }break;
            case NS:
            case CNAME:
            case SOA:
            case PTR:{
                RDATA = (value.length()+value).getBytes(StandardCharsets.UTF_8);
                RDATAFormat = value;
            }break;
            default:{
                throw new IllegalArgumentException("当前类型还未实现!");
            }
        }
    }

    @Override
    public void read(DataInputStream dataInputStream) throws IOException {
        super.read(dataInputStream);
        //获取资源记录缓存时间,4字节的无符号整数
        TTL = dataInputStream.readInt();
        //读取RDATA长度
        int length = dataInputStream.readUnsignedShort();
        RDATA = new byte[length];
        for(int i=0;i<RDATA.length;i++){
            RDATA[i] = dataInputStream.readByte();
        }
        ByteArrayInputStream bais = new ByteArrayInputStream(RDATA);
        DataInputStream mxDataInputStream = new DataInputStream(bais);
        QTYPE qtype = getQTYPE();
        switch (qtype){
            case A:{
                RDATAFormat = (RDATA[0]&0xFF) + "." + (RDATA[1]&0xFF) + "." + (RDATA[2]&0xFF) + "." + (RDATA[3]&0xFF);
            }break;
            case MX:{
                RDATAFormat = "(" + mxDataInputStream.readShort() + ")" + readDomainName(mxDataInputStream);
            }break;
            case NS:
            case CNAME:
            case SOA:
            case PTR:{
                RDATAFormat = readDomainName(mxDataInputStream);
            }break;
        }
    }

    @Override
    public void write(DataOutputStream dataOutputStream) throws IOException {
        super.write(dataOutputStream);
        dataOutputStream.writeInt(TTL);
        dataOutputStream.writeShort(RDATA.length);
        for(byte byte1:RDATA){
            dataOutputStream.writeByte(byte1);
        }
    }

    @Override
    public String toString() {
        return "\n{\n" +
                "查询域名:" + getQNAME() + "\n"
                + "查询类型:" + getQTYPE().name() + "\n"
                + "查询协议类:" + getQCLASS().name() + "\n"
                + "缓存时间(秒):" + getTTL() + "\n"
                + "数据:" + RDATAFormat + "\n"
                + "}\n";
    }
}
