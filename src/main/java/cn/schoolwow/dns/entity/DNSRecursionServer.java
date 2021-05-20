package cn.schoolwow.dns.entity;

import cn.schoolwow.quickdao.annotation.*;

import java.time.LocalDateTime;

@Comment("DNS递归查询服务器")
@UniqueField(columns = {"dnsServerIP","order"})
public class DNSRecursionServer {
    @Id
    private long id;

    @Comment("备注")
    private String remark;

    @Comment("上级DNS服务IP")
    @Constraint(notNull = true)
    private String dnsServerIP;

    @Comment("优先顺序(越小越优先)")
    @Index(indexType = IndexType.UNIQUE)
    @Constraint(notNull = true)
    private int order;

    @Comment("是否启用")
    @Constraint(notNull = true,defaultValue = "true")
    private boolean enable;

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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDnsServerIP() {
        return dnsServerIP;
    }

    public void setDnsServerIP(String dnsServerIP) {
        this.dnsServerIP = dnsServerIP;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
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
