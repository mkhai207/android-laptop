package com.example.android_doan.data.api.user;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiService {
    private static final String BASE_URL = "http://192.168.50.2:8080/";
    private static Api instance;

    public static Api getInstance(){
        if (instance == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(HomeOkHttpClient.getOkHttpClient())
                    .build();
            instance = retrofit.create(Api.class);
        }
        return instance;
    }
}
