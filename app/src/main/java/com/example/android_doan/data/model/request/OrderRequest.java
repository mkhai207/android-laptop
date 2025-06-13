package com.example.android_doan.data.model.request;

import com.example.android_doan.data.enums.OrderStatusEnum;
import com.example.android_doan.data.model.UserModel;
import com.example.android_doan.data.model.response.AddressResponse;
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

    private AddressResponse address;

    public OrderRequest(OrderStatusEnum status, String paymentMethod, UserModel user, List<OrderDetail> orderDetails, AddressResponse address) {
        this.status = status;
        this.paymentMethod = paymentMethod;
        this.user = user;
        this.orderDetails = orderDetails;
        this.address = address;
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
