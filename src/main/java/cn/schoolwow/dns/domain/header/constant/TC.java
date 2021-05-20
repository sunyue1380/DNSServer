package cn.schoolwow.dns.domain.header.constant;

/**
 * Truncation 是否截断
 * 应答总长度超过512字节时,只返回前512字节内容,同时置此位为1
 * */
public enum TC {
    //未截断
    NOT_TRUNCATE(0),
    //截断
    TRUNCATE(1);

    public int value;

    TC(int value) {
        this.value = value;
    }

    public static TC getTC(int value){
        if(value==0){
            return NOT_TRUNCATE;
        }else if(value==1){
            return TRUNCATE;
        }else{
            throw new IllegalArgumentException("不合法的TC值!值:"+value);
        }
    }
}
