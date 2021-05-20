package cn.schoolwow.dns.dto;

import java.util.List;

public class OptionSource {
    /**
     * 下拉框选项
     * */
    private List<OptionDTO> options;

    /**
     * 默认值
     * */
    private String value;

    public List<OptionDTO> getOptions() {
        return options;
    }

    public void setOptions(List<OptionDTO> options) {
        this.options = options;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
