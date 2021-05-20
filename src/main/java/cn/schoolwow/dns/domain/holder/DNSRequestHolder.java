package cn.schoolwow.dns.domain.holder;

import java.util.HashMap;
import java.util.Map;

/**
 * DNS报文数据类
 * */
public class DNSRequestHolder {
    /**报文数据*/
    public byte[] data;

    /**缓存字符串标量*/
    public Map<Integer,String> pointerStringCache = new HashMap<>();
}
