package com.iquickmove.base.request;

import java.util.Objects;

/**
 * @author elijah
 */
public abstract class PageRequest extends AbstractRequest {

    private static final long serialVersionUID = 1L;

    /**
     * 页码
     */
    private Integer page = 1; //默认第一页

    /**
     * 页大小
     * 默认10
     */
    private Integer pageSize = 20;

    /**
     * 记录起始位置
     */
    private Integer start = 0;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getStart() {
        return page != null && page.intValue() > 0 ? (page - 1) * pageSize : 0;
    }
}
