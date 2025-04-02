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
    private String createdAt;
    private String updatedAt;

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

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }
}
