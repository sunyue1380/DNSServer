package cn.schoolwow.dns.domain.header.constant;

/**Operation code 查询类型*/
public enum OPCODE {
    //标准查询
    STANDARD_QUERY(0),
    //反向查询
    REVERSE_QUERY(1),
    //服务器状态查询,
    STATUS_QUERY(2);

    public int value;

    OPCODE(int value) {
        this.value = value;
    }

    public static OPCODE getOPCODE(int value){
        switch (value){
            case 0:return STANDARD_QUERY;
            case 1:return REVERSE_QUERY;
            case 2:return STATUS_QUERY;
            default:{
                throw new IllegalArgumentException("不合法的OPCODE值!值:"+value);
            }
        }
    }
}
