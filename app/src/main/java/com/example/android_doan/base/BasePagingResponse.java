package com.example.android_doan.base;

import com.example.android_doan.data.model.response.Meta;

import java.util.List;

public class BasePagingResponse<T> {
    private Meta meta;
    private List<T> result;

    public Meta getMeta() {
        return meta;
    }

    public List<T> getResult() {
        return result;
    }
}
