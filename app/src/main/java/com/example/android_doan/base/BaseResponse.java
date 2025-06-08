package com.example.android_doan.base;

public class BaseResponse<T> {
    private int statusCode;
    private String error;
    private String message;

    private T data;

    public int getStatusCode() {
        return statusCode;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }
}
