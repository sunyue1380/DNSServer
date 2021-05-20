package cn.schoolwow.dns.entity;

import cn.schoolwow.quickdao.annotation.*;

import java.time.LocalDateTime;

@Comment("DNS记录表")
public class DNSRecord {
    @Id
    private long id;

    @Comment("域名")
    @Constraint(notNull = true)
    @Index(indexType = IndexType.UNIQUE)
    private String domain;

    @Comment("类型")
    @Constraint(notNull = true)
    private String type;

    @Comment("缓存时间(秒)")
    @Constraint(notNull = true,defaultValue = "600")
    private int ttl;

    @Comment("记录值")
    @Constraint(notNull = true)
    private String value;

    @Comment("创建时间")
    @TableField(createdAt = true)
    private LocalDateTime createdTime;

    @Comment("更新时间")
    @TableField(createdAt = true)
    private LocalDateTime updateTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
}
