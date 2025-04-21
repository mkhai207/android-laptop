package com.example.android_doan.data.model.response;

import com.example.android_doan.data.model.CategoryModel;

public class CreateCategoryResponse {
    private int statusCode;
    private String error;
    private String message;
    private CategoryModel data;

    public int getStatusCode() {
        return statusCode;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public CategoryModel getData() {
        return data;
    }
}
