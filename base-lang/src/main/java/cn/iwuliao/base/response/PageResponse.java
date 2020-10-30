package cn.iwuliao.base.response;

import java.util.ArrayList;
import java.util.List;

/**
 * @author elijah
 */
public class PageResponse<T> extends CommonResponse {

    private static final long serialVersionUID = 1L;
    /**
     * 当前页
     */
    private int page;
    /**
     * 总共条数
     */
    private int total;
    /**
     * 具体数据
     */
    private List<T> data;


    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
