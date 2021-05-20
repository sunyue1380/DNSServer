package cn.schoolwow.dns.entity.ignore;

import cn.schoolwow.quickdao.annotation.Comment;
import cn.schoolwow.quickdao.annotation.Ignore;

@Ignore
public class DNSHeaderQuestionRecord {
    @Comment("标识id")
    private int transactionId;

    @Comment("查询类型")
    private String qr;

    @Comment("操作类型")
    private String opcode;

    @Comment("是否权威回答")
    private boolean aa;

    @Comment("是否截断")
    private boolean tc;

    @Comment("客户端请求递归")
    private String rd;

    @Comment("服务端响应递归")
    private String ra;

    @Comment("响应类型")
    private String rcode;

    @Comment("域名")
    private String qname;

    @Comment("问题类型")
    private String qtype;

    @Comment("查询协议类")
    private String qclass;

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public String getQr() {
        return qr;
    }

    public void setQr(String qr) {
        this.qr = qr;
    }

    public String getOpcode() {
        return opcode;
    }

    public void setOpcode(String opcode) {
        this.opcode = opcode;
    }

    public boolean isAa() {
        return aa;
    }

    public void setAa(boolean aa) {
        this.aa = aa;
    }

    public boolean isTc() {
        return tc;
    }

    public void setTc(boolean tc) {
        this.tc = tc;
    }

    public String getRd() {
        return rd;
    }

    public void setRd(String rd) {
        this.rd = rd;
    }

    public String getRa() {
        return ra;
    }

    public void setRa(String ra) {
        this.ra = ra;
    }

    public String getRcode() {
        return rcode;
    }

    public void setRcode(String rcode) {
        this.rcode = rcode;
    }

    public String getQname() {
        return qname;
    }

    public void setQname(String qname) {
        this.qname = qname;
    }

    public String getQtype() {
        return qtype;
    }

    public void setQtype(String qtype) {
        this.qtype = qtype;
    }

    public String getQclass() {
        return qclass;
    }

    public void setQclass(String qclass) {
        this.qclass = qclass;
    }
}
