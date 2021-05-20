package cn.schoolwow.dns.domain.question.constants;

/**查询类型*/
public enum QTYPE {
    //Host (A) record
    A(1),
    //Name server (NS) record
    NS(2),
    //Alias (CNAME) record
    CNAME(5),
    //marks the start of a zone of authority
    SOA(6),
    //a well known service description
    WKS(11),
    //Reverse-lookup (PTR) record
    PTR(12),
    //Mail exchange (MX) record
    MX(15),
    //text strings
    TXT(16),
    //Service (SRV) record
    SRV(33),
    //Incremental zone transfer (IXFR) record
    IXFR(251),
    //Standard zone transfer (AXFR) record
    AXFR(252),
    //All records
    ALL(255);

    public int value;

    QTYPE(int value) {
        this.value = value;
    }

    public static QTYPE getQTYPE(short value){
        switch (value){
            case 1:return A;
            case 2:return NS;
            case 5:return CNAME;
            case 6:return SOA;
            case 11:return WKS;
            case 12:return PTR;
            case 15:return MX;
            case 16:return TXT;
            case 33:return SRV;
            case 251:return IXFR;
            case 252:return AXFR;
            case 255:return ALL;
            default:{
                throw new IllegalArgumentException("不支持的QTYPE类型!当前值:"+value);
            }
        }
    }
}
