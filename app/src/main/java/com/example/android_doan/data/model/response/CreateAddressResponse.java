package com.example.android_doan.data.model.response;

public class CreateAddressResponse {
    private int statusCode;
    private String error;
    private String message;
    private AddressResponse data;

    public int getStatusCode() {
        return statusCode;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public AddressResponse getData() {
        return data;
    }
}
