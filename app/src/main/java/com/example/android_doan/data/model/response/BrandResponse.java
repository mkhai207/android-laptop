package com.example.android_doan.data.model.response;

import com.example.android_doan.data.model.BrandModel;

import java.util.List;

public class BrandResponse {
    private int statusCode;
    private String error;
    private String message;
    private WrapperData data;

    public int getStatusCode() {
        return statusCode;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public WrapperData getData() {
        return data;
    }

    public static class WrapperData{
        private Meta meta;
        private List<BrandModel> result;

        public Meta getMeta() {
            return meta;
        }
        public List<BrandModel> getResult() {
            return result;
        }
    }
}
