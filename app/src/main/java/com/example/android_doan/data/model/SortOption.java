package com.example.android_doan.data.model;

public class SortOption {
    private String realData;
    private String name;

    public SortOption(String realData, String name) {
        this.realData = realData;
        this.name = name;
    }

    public String getRealData() {
        return realData;
    }

    public String getName() {
        return name;
    }
}
