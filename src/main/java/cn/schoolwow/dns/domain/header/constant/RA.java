package cn.schoolwow.dns.domain.header.constant;

/**Recursion available 服务器是否可以递归解析*/
public enum RA {
    //服务器无法进行递归解析
    CAN_NOT_RECURSIVE(0),
    //服务器可以进行递归解析
    CAN_RECURSIVE (1);

    public int value;

    RA(int value) {
        this.value = value;
    }

    public static RA getRA(int value){
        if(value==0){
            return CAN_NOT_RECURSIVE;
        }else if(value==1){
            return CAN_RECURSIVE;
        }else{
            throw new IllegalArgumentException("不合法的RA值!值:"+value);
        }
    }
}
