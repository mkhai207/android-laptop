package com.example.android_doan.data.model.response;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("data")
    private LoginData data;

    public LoginData getData() {
        return data;
    }
}
