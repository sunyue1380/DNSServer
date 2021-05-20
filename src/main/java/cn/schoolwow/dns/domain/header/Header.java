package cn.schoolwow.dns.domain.header;

import cn.schoolwow.dns.domain.header.constant.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**DNS报文头部*/
public class Header {
    /**标识字段*/
    private int id;

    /**查询类型*/
    private QR qr = QR.QUERY;

    /**操作类型*/
    private OPCODE opcode = OPCODE.STANDARD_QUERY;

    /**是否权威回答*/
    private AA aa = AA.NOT_AUTHORITATIVE_ANSWER;

    /**是否截断*/
    private TC tc = TC.NOT_TRUNCATE;

    /**客户端请求递归*/
    private RD rd = RD.RECURSIVE;

    /**服务端响应递归*/
    private RA ra = RA.CAN_NOT_RECURSIVE;

    /**获取响应类型*/
    private RCODE rcode = RCODE.SUCCESS;

    /**问题个数*/
    private int questionCount;

    /**答案个数*/
    private int answerResourceRecordCount;

    /**权威资源个数*/
    private int authorityResourceRecordCount;

    /**附加资源个数*/
    private int additionalResourceRecordCount;

    public Header() {
    }

    public int getID() {
        return id;
    }

    public void setID(int id) {
        this.id = id;
    }

    public QR getQR() {
        return qr;
    }

    public void setQR(QR qr) {
        this.qr = qr;
    }

    public OPCODE getOPCODE() {
        return opcode;
    }

    public void setOPCODE(OPCODE opcode) {
        this.opcode = opcode;
    }

    public AA getAA() {
        return aa;
    }

    public void setAA(AA aa) {
        this.aa = aa;
    }

    public TC getTC() {
        return tc;
    }

    public void setTC(TC tc) {
        this.tc = tc;
    }

    public RD getRD() {
        return rd;
    }

    public void setRD(RD rd) {
        this.rd = rd;
    }

    public RA getRA() {
        return ra;
    }

    public void setRA(RA ra) {
        this.ra = ra;
    }

    public RCODE getRCODE() {
        return rcode;
    }

    public void setRCODE(RCODE rcode) {
        this.rcode = rcode;
    }

    public int getQuestionCount() {
        return questionCount;
    }

    public void setQuestionCount(int questionCount) {
        this.questionCount = questionCount;
    }

    public int getAnswerResourceRecordCount() {
        return answerResourceRecordCount;
    }

    public void setAnswerResourceRecordCount(int answerResourceRecordCount) {
        this.answerResourceRecordCount = answerResourceRecordCount;
    }

    public int getAuthorityResourceRecordCount() {
        return authorityResourceRecordCount;
    }

    public void setAuthorityResourceRecordCount(int authorityResourceRecordCount) {
        this.authorityResourceRecordCount = authorityResourceRecordCount;
    }

    public int getAdditionalResourceRecordCount() {
        return additionalResourceRecordCount;
    }

    public void setAdditionalResourceRecordCount(int additionalResourceRecordCount) {
        this.additionalResourceRecordCount = additionalResourceRecordCount;
    }

    /**从输入流读取信息*/
    public void read(DataInputStream dataInputStream) throws IOException {
        id = dataInputStream.readUnsignedShort();

        short flag = dataInputStream.readShort();
        //标志位
        qr = QR.getQR(getBitValue(flag,1));
        opcode = OPCODE.getOPCODE(getBitValue(flag,2,4));
        aa = AA.getAA(getBitValue(flag,5));
        tc = TC.getTC(getBitValue(flag,6));
        rd = RD.getRD(getBitValue(flag,7));
        ra = RA.getRA(getBitValue(flag,8));
        rcode = RCODE.getRCODE(getBitValue(flag,12,16));

        questionCount = dataInputStream.readShort();
        answerResourceRecordCount = dataInputStream.readShort();
        authorityResourceRecordCount = dataInputStream.readShort();
        additionalResourceRecordCount = dataInputStream.readShort();
    }

    /**输出到字节流*/
    public void write(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeShort(id);
        int flag = 0;
        if(qr.value==1){
            flag += 0x8000;
        }
        flag = (opcode.value<<12)|flag;
        if(aa.value==1){
            flag += 0x800;
        }
        if(tc.value==1){
            flag += 0x400;
        }
        if(rd.value==1){
            flag += 0x200;
        }
        if(ra.value==1){
            flag += 0x100;
        }
        flag += rcode.value;
        dataOutputStream.writeShort(flag);
        dataOutputStream.writeShort(questionCount);
        dataOutputStream.writeShort(answerResourceRecordCount);
        dataOutputStream.writeShort(authorityResourceRecordCount);
        dataOutputStream.writeShort(additionalResourceRecordCount);
    }

    @Override
    public String toString() {
        return "\n{\n" +
                "标识:" + getID() + "\n"
                + "查询类型:" + getQR().name() + "\n"
                + "操作类型:" + getOPCODE().name() + "\n"
                + "权威回答:" + getAA().name() + "\n"
                + "截断:" + getTC().name() + "\n"
                + "客户端递归查询:" + getRD().name() + "\n"
                + "服务端递归查询:" + getRA().name() + "\n"
                + "响应类型:" + getRCODE().name() + "\n"
                + "问题个数:" + getQuestionCount() + "\n"
                + "答案个数:" + getAnswerResourceRecordCount() + "\n"
                + "认证资源个数:" + getAuthorityResourceRecordCount() + "\n"
                + "附加资源个数:" + getAdditionalResourceRecordCount() + "\n"
                + "}\n";
    }

    /**获取指定二进制位的值*/
    private int getBitValue(int value, int index) {
        return getBitValue(value, index,index);
    }

    /**获取指定二进制位的值*/
    private int getBitValue(int value, int startIndex, int endIndex){
        if(startIndex>endIndex){
            throw new IllegalArgumentException("start必须小于等于end!");
        }
        if(startIndex==endIndex){
            return (value>>(16-startIndex)&0x01);
        }
        int length = (1<<(endIndex-startIndex+1))-1;
        return ((value>>(16-endIndex))&length);
    }
}
