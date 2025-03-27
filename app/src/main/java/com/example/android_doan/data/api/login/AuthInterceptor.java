package com.example.android_doan.data.api.login;

import androidx.annotation.NonNull;

import com.example.android_doan.data.repository.LocalRepository.DataLocalManager;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {
    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request originalRequest = chain.request();
        String refreshToken = DataLocalManager.getRefreshToken();

        if (refreshToken != null && !refreshToken.isEmpty()){
            Request newRequest = originalRequest.newBuilder()
                    .addHeader("Cookie", "refresh_token=" + refreshToken)
                    .build();
            return chain.proceed(newRequest);
        }
        return chain.proceed(originalRequest);
    }
}
