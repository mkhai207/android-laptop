package com.example.android_doan.data.model.response;

import com.example.android_doan.data.model.UserModel;

public class UserResponse {
    private int statusCode;
    private String error;
    private String message;
    private UserModel data;

    public int getStatusCode() {
        return statusCode;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public UserModel getData() {
        return data;
    }
}
