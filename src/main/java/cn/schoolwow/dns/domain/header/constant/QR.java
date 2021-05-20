package cn.schoolwow.dns.domain.header.constant;

/**Request/response 操作类型*/
public enum QR {
    //查询报文
    QUERY(0),
    //响应报文
    RESPONSE(1);

    public int value;

    QR(int value) {
        this.value = value;
    }

    public static QR getQR(int value){
        if(value==0){
            return QUERY;
        }else if(value==1){
            return RESPONSE;
        }else{
            throw new IllegalArgumentException("不合法的QR值!值:"+value);
        }
    }
}
