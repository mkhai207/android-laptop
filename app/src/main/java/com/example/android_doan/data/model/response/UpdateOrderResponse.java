package com.example.android_doan.data.model.response;

public class UpdateOrderResponse {
    private int statusCode;
    private String error;
    private String message;
    private OrderAdminResponse data;

    public int getStatusCode() {
        return statusCode;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public OrderAdminResponse getData() {
        return data;
    }
}
