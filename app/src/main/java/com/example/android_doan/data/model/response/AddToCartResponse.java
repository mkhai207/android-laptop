package com.example.android_doan.data.model.response;

public class AddToCartResponse {
    private int statusCode;
    private String error;
    private String message;

    public int getStatusCode() {
        return statusCode;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }
}
