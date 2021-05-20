package cn.schoolwow.dns.dto;

import java.util.List;

public class CRUDDTO<T> {
    /**
     * 实际数据
     */
    private List<T> items;

    /**
     * 数据总数
     */
    private long total;

    /**
     * 是否需要分页
     */
    private boolean loadDataOnce;

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public boolean isLoadDataOnce() {
        return loadDataOnce;
    }

    public void setLoadDataOnce(boolean loadDataOnce) {
        this.loadDataOnce = loadDataOnce;
    }
}
