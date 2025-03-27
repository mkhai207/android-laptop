package com.example.android_doan.data.api.user;

import com.example.android_doan.data.api.login.AuthInterceptor;
import com.example.android_doan.data.api.login.TokenAuthenticator;

import okhttp3.OkHttpClient;

public class HomeOkHttpClient {
    public static OkHttpClient getOkHttpClient(){
        return new OkHttpClient.Builder()
                .addInterceptor(new HomeInterceptor())
                .authenticator(new TokenAuthenticator())
                .build();
    }
}
