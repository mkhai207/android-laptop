package com.example.android_doan.data.model.response;

import java.util.List;

public class GetAllOrderResponse {
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

    public static class Data{
        private Meta meta;
        private List<OrderAdminResponse> result;

        public Meta getMeta() {
            return meta;
        }

        public List<OrderAdminResponse> getResult() {
            return result;
        }
    }
}
