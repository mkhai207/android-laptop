package com.example.android_doan.data.model;

import java.io.Serializable;

public class UserModel implements Serializable {
    private int id;
    private boolean active;
    private String fullName;
    private String address;
    private String email;
    private String phone;
    private String gender;
    private String birthday;
    private String shoppingAddress;
    private String avatar;
    private RoleModel role;
    public UserModel(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public boolean isActive() {
        return active;
    }

    public String getFullName() {
        return fullName;
    }

    public String getAddress() {
        return address;
    }
    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getGender() {
        return gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getShoppingAddress() {
        return shoppingAddress;
    }

    public String getAvatar() {
        return avatar;
    }

    public RoleModel getRole() {
        return role;
    }

    public UserModel(int id, boolean active, String avatar, String fullName, String address, String phone, String gender, String birthday, String shoppingAddress) {
        this.id = id;
        this.active = active;
        this.avatar = avatar;
        this.fullName = fullName;
        this.address = address;
        this.phone = phone;
        this.gender = gender;
        this.birthday = birthday;
        this.shoppingAddress = shoppingAddress;
    }
}
