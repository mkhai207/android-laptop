package com.example.android_doan.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class OrderDetail implements Serializable {
    @SerializedName("id")
    private int id;

    @SerializedName("price")
    private double price;

    @SerializedName("quantity")
    private int quantity;

    @SerializedName("productName")
    private String productName;

    @SerializedName("productThumbnail")
    private String productThumbnail;

    public int getId() {
        return id;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductThumbnail() {
        return productThumbnail;
    }
}