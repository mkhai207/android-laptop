package com.example.android_doan.data.api.login;

import androidx.annotation.NonNull;

import com.example.android_doan.data.repository.LocalRepository.DataLocalManager;

import java.io.IOException;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.Response;

public class LoginInterceptor implements Interceptor {
    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        String path = chain.request().url().encodedPath();
        
        List<String> cookies = response.headers("Set-Cookie");
        for (String cookie: cookies){
            if (cookie.startsWith("refresh_token")){
                String refreshToken = cookie.split(";")[0].split("=")[1];
                DataLocalManager.saveRefreshToken(refreshToken);
            }
        }
        return response;
    }
}
