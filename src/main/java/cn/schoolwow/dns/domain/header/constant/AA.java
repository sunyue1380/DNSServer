package cn.schoolwow.dns.domain.header.constant;

/**Authoritative answer 权威回答*/
public enum AA {
    //非权威回答
    NOT_AUTHORITATIVE_ANSWER(0),
    //权威回答
    AUTHORITATIVE_ANSWER(1);

    public int value;

    AA(int value) {
        this.value = value;
    }

    public static AA getAA(int value){
        if(value==0){
            return NOT_AUTHORITATIVE_ANSWER;
        }else if(value==1){
            return AUTHORITATIVE_ANSWER;
        }else{
            throw new IllegalArgumentException("不合法的AA值!值:"+value);
        }
    }
}
