package com.example.android_doan.data.model.response;

import com.google.gson.annotations.SerializedName;

public class UserResponse {
    @SerializedName("data")
    private User user;

    public User getUser() {
        return user;
    }
}
