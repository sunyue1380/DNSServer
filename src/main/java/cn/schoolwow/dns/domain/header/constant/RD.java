package cn.schoolwow.dns.domain.header.constant;

/**Recursion desired 是否递归查询*/
public enum RD {
    //迭代查询
    ITERATIVE(0),
    //递归查询
    RECURSIVE(1);

    public int value;

    RD(int value) {
        this.value = value;
    }

    public static RD getRD(int value){
        if(value==0){
            return ITERATIVE;
        }else if(value==1){
            return RECURSIVE;
        }else{
            throw new IllegalArgumentException("不合法的RD值!值:"+value);
        }
    }
}
