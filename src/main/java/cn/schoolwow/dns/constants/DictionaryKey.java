package cn.schoolwow.dns.constants;

public enum DictionaryKey {
    ;
    /**默认值*/
    public String defaultValue;

    /**描述*/
    public String description;

    DictionaryKey(String defaultValue, String description) {
        this.defaultValue = defaultValue;
        this.description = description;
    }
}
