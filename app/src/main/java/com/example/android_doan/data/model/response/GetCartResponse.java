package com.example.android_doan.data.model.response;

import com.example.android_doan.data.model.ProductModel;

import java.util.List;

public class GetCartResponse {
    private int statusCode;
    private String error;
    private String message;

    private List<Data> data;

    public static class Data {
        private int quantity;
        private ProductModel product;

        public int getQuantity() {
            return quantity;
        }

        public ProductModel getProduct() {
            return product;
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

    public List<Data> getData() {
        return data;
    }
}
