package cn.schoolwow.dns.domain.header.constant;

/**Return code 返回码*/
public enum RCODE {
    //标准查询
    SUCCESS(0),
    //格式错误
    FORMAT_ERROR(1),
    //服务器响应失败,
    SERVER_FAILURE(2),
    //域名不存在
    NON_EXISTENT_DOMAIN(3),
    //未实现
    NOT_IMPLEMENTED(4),
    //请求拒绝
    QUERY_REFUSED(5);

    public int value;

    RCODE(int value) {
        this.value = value;
    }

    public static RCODE getRCODE(int value){
        switch (value){
            case 0:return SUCCESS;
            case 1:return FORMAT_ERROR;
            case 2:return SERVER_FAILURE;
            case 3:return NON_EXISTENT_DOMAIN;
            case 4:return NOT_IMPLEMENTED;
            case 5:return QUERY_REFUSED;
            default:{
                throw new IllegalArgumentException("不合法的RCODE值!值:"+value);
            }
        }
    }
}
