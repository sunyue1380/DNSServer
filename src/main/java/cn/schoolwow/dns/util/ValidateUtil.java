package cn.schoolwow.dns.util;

import java.lang.reflect.Array;
import java.util.Collection;

public class ValidateUtil {
    /**是否为空指针*/
    public static boolean isNull(Object value){
        return value==null;
    }

    /**是否为空指针,不为空指针则抛出异常*/
    public static void checkNull(Object value){
        if(!isNull(value)){
            throw new ValidateException();
        }
    }

    /**是否为空指针,不为空指针则抛出异常*/
    public static void checkNull(Object value,String message){
        if(!isNull(value)){
            throw new ValidateException(message);
        }
    }

    /**是否不为空指针*/
    public static boolean isNotNull(Object value){
        return value!=null;
    }

    /**是否不为空指针,为空指针则抛出异常*/
    public static void checkNotNull(Object value){
        if(!isNotNull(value)){
            throw new ValidateException();
        }
    }

    /**是否不为空指针,为空指针则抛出异常*/
    public static void checkNotNull(Object value,String message){
        if(!isNotNull(value)){
            throw new ValidateException(message);
        }
    }

    /**是否为空字符串*/
    public static boolean isEmpty(Object value){
        if(value==null){
            return true;
        }
        if(value instanceof String){
            return ((String) value).isEmpty();
        }
        if(value.getClass().isArray()||value instanceof Array){
            Object[] oo = (Object[]) value;
            return oo.length==0;
        }
        if(value instanceof Collection){
            Collection collection = (Collection) value;
            return collection.isEmpty();
        }
        return true;
    }

    /**是否为空,不为空则抛出异常*/
    public static void checkEmpty(Object value){
        if(!isEmpty(value)){
            throw new ValidateException();
        }
    }

    /**是否为空,不为空则抛出异常*/
    public static void checkEmpty(Object value,String message){
        if(!isEmpty(value)){
            throw new ValidateException(message);
        }
    }

    /**是否不为空字符串*/
    public static boolean isNotEmpty(Object value){
        return !isEmpty(value);
    }

    /**是否不为空,为空则抛出异常*/
    public static void checkNotEmpty(Object value){
        if(!isNotEmpty(value)){
            throw new ValidateException();
        }
    }

    /**是否不为空,为空则抛出异常*/
    public static void checkNotEmpty(Object value,String message){
        if(!isNotEmpty(value)){
            throw new ValidateException(message);
        }
    }

    /**检查是否小于最小长度*/
    public static boolean isMinLength(int max,String value){
        return isLength(max,9999,value);
    }

    /**是否小于最小长度*/
    public static void checkMinLength(int max,String value){
        checkLength(max,9999,value);
    }

    /**是否小于最小长度*/
    public static void checkMinLength(int max,String value,String message){
        checkLength(max,9999,value,message);
    }

    /**检查是否大于最大长度*/
    public static boolean isMaxLength(int max,String value){
        return isLength(0,max,value);
    }

    /**是否大于最大长度*/
    public static void checkMaxLength(int max,String value){
        checkLength(0,max,value);
    }

    /**是否大于最大长度*/
    public static void checkMaxLength(int max,String value,String message){
        checkLength(0,max,value,message);
    }

    /**是否符合长度*/
    public static boolean isLength(int min,int max,String value){
        return value.length()>=min&&value.length()<=max;
    }

    /**是否符合长度*/
    public static void checkLength(int min,int max,String value){
        if(!isLength(min,max,value)){
            throw new ValidateException();
        }
    }

    /**是否符合长度*/
    public static void checkLength(int min,int max,String value,String message){
        if(!isLength(min,max,value)){
            throw new ValidateException(message);
        }
    }

    /**表达式是否为真,为假则抛出异常*/
    public static void checkArgument(boolean expression){
        if(!expression){
            throw new ValidateException();
        }
    }

    /**表达式是否为真,为假则抛出异常*/
    public static void checkArgument(boolean expression,String message){
        if(!expression){
            throw new ValidateException(message);
        }
    }

    /**模式是否匹配*/
    public static void checkPattern(String input,String pattern){
        if(!input.matches(pattern)){
            throw new ValidateException();
        }
    }
    /**模式是否匹配*/
    public static void checkPattern(String input,String pattern,String message){
        if(!input.matches(pattern)){
            throw new ValidateException(message);
        }
    }
}
