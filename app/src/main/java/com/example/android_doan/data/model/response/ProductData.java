package com.example.android_doan.data.model.response;

import com.example.android_doan.data.model.ProductModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductData {
    private Meta meta;
    @SerializedName("result")
    private List<ProductModel> products;

    public Meta getMeta() {
        return meta;
    }

    public List<ProductModel> getProducts() {
        return products;
    }
}
