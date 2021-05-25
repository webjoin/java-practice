package com.iquickmove.base.request;

public class PageInfo {

    /**
     * 页码
     */
    private int page = 1;

    /**
     * 页大小
     */
    private int pageSize = 5;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
