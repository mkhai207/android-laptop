package com.example.android_doan.data.model.response;

import com.example.android_doan.data.model.BrandModel;

public class CreateBrandResponse {
    private int statusCode;
    private String error;
    private String message;
    private BrandModel data;

    public int getStatusCode() {
        return statusCode;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public BrandModel getData() {
        return data;
    }
}
