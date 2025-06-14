package com.example.android_doan.data.model.response;

import com.example.android_doan.data.model.OrderData;
import com.example.android_doan.data.model.OrderDetail;
import com.example.android_doan.data.model.UserModel;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class OrderAdminResponse implements Serializable {
    @SerializedName("id")
    private int id;

    @SerializedName("status")
    private String status;

    @SerializedName("totalMoney")
    private double totalMoney;

    @SerializedName("paymentMethod")
    private String paymentMethod;

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("createdBy")
    private String createdBy;

    @SerializedName("updatedAt")
    private String updatedAt;

    @SerializedName("updatedBy")
    private String updatedBy;

    @SerializedName("name")
    private String name;
    @SerializedName("shippingAddress")
    private String shippingAddress;
    @SerializedName("phone")
    private String phone;
    @SerializedName("user")
    private UserModel user;
    @SerializedName("address")
    private AddressResponse address;

    @SerializedName("orderDetails")
    private List<OrderDetail> orderDetails;

    public int getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public double getTotalMoney() {
        return totalMoney;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public String getName() {
        return name;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public String getPhone() {
        return phone;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public UserModel getUser() {
        return user;
    }

    public AddressResponse getAddress() {
        return address;
    }

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public OrderData mapToOrderData() {
        return new OrderData(
                this.id,
                this.status,
                this.totalMoney,
                this.paymentMethod,
                this.shippingAddress,
                this.name,
                this.phone,
                this.createdAt,
                this.createdBy,
                this.updatedAt,
                this.updatedBy,
                this.user,
                this.orderDetails
        );
    }
}
