package com.example.android_doan.data.api.login;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginService {
    private static final String BASE_URL = "http://192.168.50.2:8080/";
//    private static final String BASE_URL = "http://192.168.1.4:8080/";
    private static LoginApi instance;

    public static LoginApi getInstance(){
        if (instance == null){
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(LoginOkHttpClient.getLoginOkHttpClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
            instance = retrofit.create(LoginApi.class);
        }
        return instance;
    }
}
