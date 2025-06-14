package com.example.android_doan.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class OrderItem implements Serializable {
    @SerializedName("productId")
    private String id;

    @SerializedName("price")
    private double price;

    @SerializedName("quantity")
    private int quantity;

    @SerializedName("productName")
    private String productName;

    @SerializedName("productThumbnail")
    private String productThumbnail;

    public OrderItem(String id, double price, int quantity, String productName, String productThumbnail) {
        this.id = id;
        this.price = price;
        this.quantity = quantity;
        this.productName = productName;
        this.productThumbnail = productThumbnail;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductThumbnail() {
        return productThumbnail;
    }

    public void setProductThumbnail(String productThumbnail) {
        this.productThumbnail = productThumbnail;
    }
}
