package com.example.android_doan.data.model.response;

import com.example.android_doan.data.model.UserModel;

import java.util.List;

public class GetAllUserResponse {
    private int statusCode;
    private String error;
    private String message;
    private Data data;

    public static class Data {
        private Meta meta;
        private List<UserModel> result;

        public Meta getMeta() {
            return meta;
        }

        public List<UserModel> getResult() {
            return result;
        }
    }

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
}
