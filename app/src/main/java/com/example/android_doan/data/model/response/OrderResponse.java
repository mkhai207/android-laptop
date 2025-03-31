package com.example.android_doan.data.model.response;

import com.example.android_doan.data.model.UserModel;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class OrderResponse implements Serializable {
    @SerializedName("statusCode")
    private int statusCode;

    @SerializedName("error")
    private String error;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private OrderDataWrapper data;

    public int getStatusCode() {
        return statusCode;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public OrderDataWrapper getData() {
        return data;
    }

    public static class OrderDataWrapper implements Serializable{
        @SerializedName("meta")
        private Meta meta;

        @SerializedName("result")
        private List<OrderData> result;

        public Meta getMeta() {
            return meta;
        }

        public List<OrderData> getResult() {
            return result;
        }
    }

    public static class OrderData implements Serializable{
        @SerializedName("id")
        private int id;

        @SerializedName("status")
        private String status;

        @SerializedName("totalMoney")
        private double totalMoney;

        @SerializedName("paymentMethod")
        private String paymentMethod;

        @SerializedName("shippingAddress")
        private String shippingAddress;

        @SerializedName("name")
        private String name;

        @SerializedName("phone")
        private String phone;

        @SerializedName("createdAt")
        private String createdAt;

        @SerializedName("createdBy")
        private String createdBy;

        @SerializedName("updatedAt")
        private String updatedAt;

        @SerializedName("updatedBy")
        private String updatedBy;

        @SerializedName("user")
        private UserModel user;

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

        public String getShippingAddress() {
            return shippingAddress;
        }

        public String getName() {
            return name;
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

        public List<OrderDetail> getOrderDetails() {
            return orderDetails;
        }
    }

    public static class OrderDetail implements Serializable{
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
}