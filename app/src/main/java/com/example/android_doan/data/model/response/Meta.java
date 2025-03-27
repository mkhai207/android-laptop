package com.example.android_doan.data.model.response;

import java.io.Serializable;

public class Meta implements Serializable {
    private int page;
    private int pageSize;
    private int total;
    private int totalOfCurrentPage;
    private int pages;

    public int getPage() {
        return page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getTotal() {
        return total;
    }

    public int getTotalOfCurrentPage() {
        return totalOfCurrentPage;
    }

    public int getPages() {
        return pages;
    }
}
