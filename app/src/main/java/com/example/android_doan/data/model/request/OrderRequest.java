package com.example.android_doan.data.model.request;

import com.example.android_doan.data.enums.OrderStatusEnum;
import com.example.android_doan.data.model.UserModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderRequest {
    @SerializedName("totalMoney")
    private double totalMoney;

    @SerializedName("paymentMethod")
    private String paymentMethod;

    @SerializedName("amountPaid")
    private String amountPaid;

    @SerializedName("status")
    private OrderStatusEnum status;

    @SerializedName("shippingAddress")
    private String shippingAddress;

    @SerializedName("name")
    private String name;

    @SerializedName("phone")
    private String phone;

    @SerializedName("user")
    private UserModel user;

    @SerializedName("orderDetails")
    private List<OrderDetail> orderDetails;

    public OrderRequest(double totalMoney, String paymentMethod, String amountPaid, OrderStatusEnum status,
                        String shippingAddress, String name, String phone, UserModel user, List<OrderDetail> orderDetails) {
        this.totalMoney = totalMoney;
        this.paymentMethod = paymentMethod;
        this.amountPaid = amountPaid;
        this.status = status;
        this.shippingAddress = shippingAddress;
        this.name = name;
        this.phone = phone;
        this.user = user;
        this.orderDetails = orderDetails;
    }

    public double getTotalMoney() {
        return totalMoney;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public String getAmountPaid() {
        return amountPaid;
    }

    public OrderStatusEnum getStatus() {
        return status;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public UserModel getUser() {
        return user;
    }

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public static class OrderDetail{
        @SerializedName("price")
        private double price;

        @SerializedName("quantity")
        private int quantity;

        @SerializedName("productId")
        private String productId;

        public OrderDetail(double price, int quantity, String productId) {
            this.price = price;
            this.quantity = quantity;
            this.productId = productId;
        }
    }
}
