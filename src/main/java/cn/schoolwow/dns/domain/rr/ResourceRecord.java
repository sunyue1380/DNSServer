package cn.schoolwow.dns.domain.rr;

import cn.schoolwow.dns.domain.question.constants.QTYPE;

import java.io.IOException;

/**资源记录*/
public interface ResourceRecord {
    /**
     * 设置资源缓存时间(秒)
     * */
    void setTTL(int TTL);

    /**设置A类型答案*/
    void setRDATA(QTYPE qtype, String value) throws IOException;

    /**获取值*/
    String getRDATAFormat();
}
