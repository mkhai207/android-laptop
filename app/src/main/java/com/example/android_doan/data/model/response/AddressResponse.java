package com.example.android_doan.data.model.response;

import com.example.android_doan.data.model.UserModel;

import java.io.Serializable;

public class AddressResponse implements Serializable {
    private int id;
    private String recipientName;
    private String phoneNumber;
    private String street;
    private String ward;
    private String district;
    private String city;
    private boolean isDefault;
    private UserModel user;

    public AddressResponse(int id) {
        this.id = id;
    }

    public AddressResponse(int id, String recipientName, String phoneNumber, String street, String ward, String district, String city, boolean isDefault, UserModel user) {
        this.id = id;
        this.recipientName = recipientName;
        this.phoneNumber = phoneNumber;
        this.street = street;
        this.ward = ward;
        this.district = district;
        this.city = city;
        this.isDefault = isDefault;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getStreet() {
        return street;
    }

    public String getWard() {
        return ward;
    }

    public String getDistrict() {
        return district;
    }

    public String getCity() {
        return city;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public UserModel getUser() {
        return user;
    }
}
