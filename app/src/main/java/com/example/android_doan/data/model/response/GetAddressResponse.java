package com.example.android_doan.data.model.response;

import java.util.List;

public class GetAddressResponse {
    private int statusCode;
    private String error;
    private String message;

    private Data data;

    public int getStatusCode() {
        return statusCode;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public Data getData() {
        return data;
    }

    public class Data{
        private Meta meta;
        private List<AddressResponse> result;

        public Meta getMeta() {
            return meta;
        }

        public List<AddressResponse> getResult() {
            return result;
        }
    }
}
