package com.example.android_doan.data.model.request;

import com.example.android_doan.data.enums.OrderStatusEnum;
import com.example.android_doan.data.model.UserModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderRequest {
    @SerializedName("status")
    private OrderStatusEnum status;

    @SerializedName("paymentMethod")
    private String paymentMethod;
    @SerializedName("user")
    private UserModel user;

    @SerializedName("orderDetails")
    private List<OrderDetail> orderDetails;

    //    private AddressResponse address;
    @SerializedName("shippingAddress")
    private String shippingAddress;
    @SerializedName("name")
    private String name;
    @SerializedName("phone")
    private String phone;

    public OrderRequest(OrderStatusEnum status, String paymentMethod, UserModel user, List<OrderDetail> orderDetails, String shippingAddress, String name, String phone) {
        this.status = status;
        this.paymentMethod = paymentMethod;
        this.user = user;
        this.orderDetails = orderDetails;
        this.shippingAddress = shippingAddress;
        this.name = name;
        this.phone = phone;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public OrderStatusEnum getStatus() {
        return status;
    }

    public UserModel getUser() {
        return user;
    }

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public static class OrderDetail {
//        @SerializedName("price")
//        private double price;

        @SerializedName("quantity")
        private int quantity;

        @SerializedName("productId")
        private String productId;

        public OrderDetail(int quantity, String productId) {
//            this.price = price;
            this.quantity = quantity;
            this.productId = productId;
        }
    }
}
