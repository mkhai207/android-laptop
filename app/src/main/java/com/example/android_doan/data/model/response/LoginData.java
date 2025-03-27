package com.example.android_doan.data.model.response;

import com.example.android_doan.data.model.UserModel;
import com.google.gson.annotations.SerializedName;

public class LoginData {
    private UserModel user;

    @SerializedName("access_token")
    private String accessToken;

    public UserModel getUser() {
        return user;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
