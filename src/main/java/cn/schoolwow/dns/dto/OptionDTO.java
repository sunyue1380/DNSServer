package cn.schoolwow.dns.dto;

/**
 * 下拉框
 */
public class OptionDTO {
    /**
     * 标签
     */
    private String label;

    /**
     * 值
     */
    private String value;

    public OptionDTO() {
    }

    public OptionDTO(String label, String value) {
        this.label = label;
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
