package com.example.android_doan.data.model.request;

import com.example.android_doan.data.model.RoleModel;

public class CreateUserRequest {
    private String email;
    private String password;
    private String fullName;
    private String address;
    private String avatar;
    private String birthday;
    private String gender;
    private String phone;
//    private String shoppingAddress;
    private RoleModel role;

    public CreateUserRequest(String email, String password, String fullName, String address, String avatar,
                       String birthday, String gender, String phone, RoleModel role) {
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.address = address;
        this.avatar = avatar;
        this.birthday = birthday;
        this.gender = gender;
        this.phone = phone;
        this.role = role;
    }
}
