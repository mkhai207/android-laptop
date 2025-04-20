package com.example.android_doan.data.api.user;

import okhttp3.logging.HttpLoggingInterceptor;

public class LoggingInterceptor {
    public static HttpLoggingInterceptor create() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return loggingInterceptor;
    }
}
