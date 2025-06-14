package com.example.android_doan.data.model.request;

import com.example.android_doan.data.model.OrderItem;
import com.example.android_doan.data.model.UserModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CreateAdminOrderRequest {

    @SerializedName("status")
    private String status;
    @SerializedName("paymentMethod")
    private String paymentMethod;
    @SerializedName("orderDetails")
    private List<OrderItem> orderDetails;
    @SerializedName("user")
    private UserModel user;
    @SerializedName("shippingAddress")
    private String shippingAddress;
    @SerializedName("name")
    private String name;
    @SerializedName("phone")
    private String phone;

    public CreateAdminOrderRequest(String status, String paymentMethod, List<OrderItem> orderDetails, UserModel user, String shippingAddress, String name, String phone) {
        this.status = status;
        this.paymentMethod = paymentMethod;
        this.orderDetails = orderDetails;
        this.user = user;
        this.shippingAddress = shippingAddress;
        this.name = name;
        this.phone = phone;
    }
}
