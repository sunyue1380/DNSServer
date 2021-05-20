package cn.schoolwow.dns.entity;

import cn.schoolwow.quickdao.annotation.ColumnType;
import cn.schoolwow.quickdao.annotation.Comment;
import cn.schoolwow.quickdao.annotation.Id;
import cn.schoolwow.quickdao.annotation.TableField;

import java.sql.Blob;
import java.time.LocalDateTime;

@Comment("DNS错误报文记录")
public class DNSErrorDatagram {
    @Id
    private long id;

    @Comment("数据报文")
    private Blob blob;

    @Comment("是否正常解析")
    private boolean normal;

    @Comment("异常提示")
    private String message;

    @Comment("报文内容")
    @ColumnType("TEXT")
    private String content;

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

    public Blob getBlob() {
        return blob;
    }

    public void setBlob(Blob blob) {
        this.blob = blob;
    }

    public boolean isNormal() {
        return normal;
    }

    public void setNormal(boolean normal) {
        this.normal = normal;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
