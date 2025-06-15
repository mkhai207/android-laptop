package com.example.android_doan.data.model.request;

import com.google.gson.annotations.SerializedName;

public class UpdateOrderRequest {
    @SerializedName("id")
    private int id;
    @SerializedName("status")

    private String status;
    //    private AddressResponse address;
    @SerializedName("shippingAddress")
    private String shippingAddress = null;

    @SerializedName("name")
    private String name = null;

    @SerializedName("phone")
    private String phone = null;

    public UpdateOrderRequest(int id, String status) {
        this.id = id;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
