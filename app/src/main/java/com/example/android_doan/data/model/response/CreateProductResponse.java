package com.example.android_doan.data.model.response;

import com.example.android_doan.data.model.ProductModel;

public class CreateProductResponse {
    private int statusCode;
    private String error;
    private String message;
    private ProductModel data;

    public int getStatusCode() {
        return statusCode;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public ProductModel getData() {
        return data;
    }
}
