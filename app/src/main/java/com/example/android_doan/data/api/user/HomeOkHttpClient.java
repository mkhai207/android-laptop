package com.example.android_doan.data.api.user;


import okhttp3.OkHttpClient;

public class HomeOkHttpClient {
    public static OkHttpClient getOkHttpClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(new TokenInterceptor())
                .addInterceptor(LoggingInterceptor.create())
                .build();
    }
}
