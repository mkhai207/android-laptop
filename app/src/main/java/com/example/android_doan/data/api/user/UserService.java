package com.example.android_doan.data.api.user;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserService {
    private static final String BASE_URL = "http://192.168.50.2:8080/";
    private static UserApi instance;

    public static UserApi getInstance(){
        if (instance == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(HomeOkHttpClient.getOkHttpClient())
                    .build();
            instance = retrofit.create(UserApi.class);
        }
        return instance;
    }
}
