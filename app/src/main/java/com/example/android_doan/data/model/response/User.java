package com.example.android_doan.data.model.response;

import com.example.android_doan.data.model.UserModel;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class User implements Serializable {
    @SerializedName("user")
    private UserModel userModel;

    public UserModel getUserModel() {
        return userModel;
    }
}
