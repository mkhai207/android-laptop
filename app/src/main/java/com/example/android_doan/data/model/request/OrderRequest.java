package com.example.android_doan.data.model.request;

import com.example.android_doan.data.enums.OrderStatusEnum;
import com.example.android_doan.data.model.UserModel;
import com.example.android_doan.data.model.response.AddressResponse;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderRequest {
//    @SerializedName("totalMoney")
//    private double totalMoney;

    @SerializedName("status")
    private OrderStatusEnum status;

    @SerializedName("paymentMethod")
    private String paymentMethod;

//    @SerializedName("amountPaid")
//    private String amountPaid;

//    @SerializedName("shippingAddress")
//    private String shippingAddress;

//    @SerializedName("name")
//    private String name;
//
//    @SerializedName("phone")
//    private String phone;

    @SerializedName("user")
    private UserModel user;

    @SerializedName("orderDetails")
    private List<OrderDetail> orderDetails;


//    public double getTotalMoney() {
//        return totalMoney;
//    }
    private AddressResponse address;
    public String getPaymentMethod() {
        return paymentMethod;
    }

//    public String getAmountPaid() {
//        return amountPaid;
//    }

    public OrderStatusEnum getStatus() {
        return status;
    }

//    public String getShippingAddress() {
//        return shippingAddress;
//    }

//    public String getName() {
//        return name;
//    }
//
//    public String getPhone() {
//        return phone;
//    }


    public OrderRequest(OrderStatusEnum status, String paymentMethod, UserModel user, List<OrderDetail> orderDetails, AddressResponse address) {
        this.status = status;
        this.paymentMethod = paymentMethod;
        this.user = user;
        this.orderDetails = orderDetails;
        this.address = address;
    }

    public UserModel getUser() {
        return user;
    }

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public static class OrderDetail{
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
