package com.example.android_doan.data.api.user;

import androidx.annotation.NonNull;

import com.example.android_doan.data.repository.LocalRepository.DataLocalManager;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class TokenInterceptor implements Interceptor {
    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();

        String accessToken = DataLocalManager.getAccessToken();
        Request newRequest = request.newBuilder()
                .addHeader("Authorization", "Bearer " + accessToken)
                .build();
        return chain.proceed(newRequest);
    }
}