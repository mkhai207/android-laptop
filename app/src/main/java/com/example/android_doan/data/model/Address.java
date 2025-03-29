package com.example.android_doan.data.model;

import java.io.Serializable;

public class Address implements Serializable {
    private String name;
    private String phone;
    private String address;

    public Address(String name, String phone, String address) {
        this.name = name;
        this.phone = phone;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }
}
