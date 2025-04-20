package com.example.android_doan.data.api.user;

import com.example.android_doan.data.api.login.TokenAuthenticator;

import okhttp3.OkHttpClient;

public class HomeOkHttpClient {
    public static OkHttpClient getOkHttpClient(){
        return new OkHttpClient.Builder()
                .addInterceptor(new TokenInterceptor())
                .authenticator(new TokenAuthenticator())
                .addInterceptor(LoggingInterceptor.create())
                .build();
    }
}
