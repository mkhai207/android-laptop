package com.example.android_doan.data.model.request;

public class UpdateCategoryRequest {
    private int id;
    private String name;
    private String code;

    public UpdateCategoryRequest(int id, String name, String code) {
        this.id = id;
        this.name = name;
        this.code = code;
    }
}
