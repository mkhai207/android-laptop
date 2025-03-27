package com.example.android_doan.data.model.request;

public class RegisterRequest {
    private String fullName;
    private String email;
    private String password;
    private String gender;

    public RegisterRequest(String fullName, String email, String password, String gender) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.gender = gender;
    }
}
