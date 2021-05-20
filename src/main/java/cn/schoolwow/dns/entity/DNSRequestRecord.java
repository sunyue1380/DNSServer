package cn.schoolwow.dns.entity;

import cn.schoolwow.dns.entity.ignore.DNSHeaderQuestionRecord;
import cn.schoolwow.quickdao.annotation.Comment;
import cn.schoolwow.quickdao.annotation.Id;
import cn.schoolwow.quickdao.annotation.TableField;

import java.time.LocalDateTime;

@Comment("DNS请求报文")
public class DNSRequestRecord extends DNSHeaderQuestionRecord {
    @Id
    private long id;

    @Comment("来源IP地址")
    private String ip;

    @Comment("客户端端口")
    private int port;

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

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
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