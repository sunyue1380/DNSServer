package cn.schoolwow.dns.entity;

import cn.schoolwow.quickdao.annotation.*;

import java.time.LocalDateTime;

@Comment("字典表")
@UniqueField(columns = "key")
public class Dictionary {
    @Id
    private long id;

    @Comment("字典键")
    private String key;

    @Comment("字典值")
    @ColumnType("varchar(1024)")
    private String value;

    @Comment("描述")
    private String description;

    /**
     * 创建时间
     */
    @Comment("创建时间")
    @TableField(createdAt = true)
    private LocalDateTime createdTime;

    /**
     * 更新时间
     */
    @Comment("更新时间")
    @TableField(updatedAt = true)
    private LocalDateTime updatedTime;

    public Dictionary() {
    }

    public Dictionary(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public Dictionary(String key, String value, String description) {
        this.key = key;
        this.value = value;
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
