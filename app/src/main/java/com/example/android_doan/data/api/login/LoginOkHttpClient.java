package com.example.android_doan.data.api.login;

import okhttp3.OkHttpClient;

public class LoginOkHttpClient {
    public static OkHttpClient getLoginOkHttpClient(){
        return new OkHttpClient.Builder()
                .addInterceptor(new LoginInterceptor())
                .build();
    }
}
