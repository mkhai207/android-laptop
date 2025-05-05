package com.example.android_doan.data.model.request;

import com.example.android_doan.data.model.UserModel;

public class CreateAddressRequest {
    private String recipientName;
    private String phoneNumber;
    private String street;
    private String ward;
    private String district;
    private String city;
    private boolean isDefault;
    private UserModel user;

    public CreateAddressRequest(String recipientName, String phoneNumber, String street, String ward, String district, String city, boolean isDefault, UserModel user) {
        this.recipientName = recipientName;
        this.phoneNumber = phoneNumber;
        this.street = street;
        this.ward = ward;
        this.district = district;
        this.city = city;
        this.isDefault = isDefault;
        this.user = user;
    }
}
