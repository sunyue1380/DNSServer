package cn.schoolwow.dns.entity;

import cn.schoolwow.dns.entity.ignore.DNSHeaderQuestionRecord;
import cn.schoolwow.quickdao.annotation.Comment;
import cn.schoolwow.quickdao.annotation.Id;
import cn.schoolwow.quickdao.annotation.TableField;

import java.time.LocalDateTime;

@Comment("DNS响应报文")
public class DNSResponseRecord extends DNSHeaderQuestionRecord {
    @Id
    private long id;

    @Comment("关联DNS请求id")
    private long dnsRequestRecordId;

    @Comment("资源缓存时间(秒)")
    private int ttl;

    @Comment("数据")
    private String value;

    @Comment("类型")
    private String resourceRecordType;

    @TableField(createdAt = true)
    private LocalDateTime createdTime;

    @TableField(updatedAt = true)
    private LocalDateTime updatedTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getDnsRequestRecordId() {
        return dnsRequestRecordId;
    }

    public void setDnsRequestRecordId(long dnsRequestRecordId) {
        this.dnsRequestRecordId = dnsRequestRecordId;
    }

    public int getTtl() {
        return ttl;
    }

    public void setTtl(int ttl) {
        this.ttl = ttl;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getResourceRecordType() {
        return resourceRecordType;
    }

    public void setResourceRecordType(String resourceRecordType) {
        this.resourceRecordType = resourceRecordType;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public LocalDateTime getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(LocalDateTime updatedTime) {
        this.updatedTime = updatedTime;
    }
}
