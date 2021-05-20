package cn.schoolwow.dns.util;

/**
 * 校验异常类
 * */
public class ValidateException extends RuntimeException{
    public ValidateException(){
        super();
    }

    public ValidateException(String message){
        super(message);
    }
}
