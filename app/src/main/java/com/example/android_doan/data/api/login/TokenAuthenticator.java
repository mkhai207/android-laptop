package com.example.android_doan.data.api.login;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.android_doan.data.model.response.LoginResponse;
import com.example.android_doan.data.repository.LocalRepository.DataLocalManager;

import java.io.IOException;

import io.reactivex.schedulers.Schedulers;
import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

public class TokenAuthenticator implements Authenticator {

    @Nullable
    @Override
    public Request authenticate(@Nullable Route route, @NonNull Response response) throws IOException {
        if (response.request().header("Retry-With-New-Token") != null) {
            Log.d("lkhai4617", "Retry-With-New-Token is not null");
            return null;
        }

        String newAccessToken = refresh();
        if (newAccessToken == null){
            Log.d("lkhai4617", "new access token is null");
            return null;
        }

        DataLocalManager.saveAccessToken(newAccessToken);
        Log.d("lkhai4617", "refresh access token");

        return response.request().newBuilder()
                .addHeader("Authorization", "Bearer " + newAccessToken)
                .addHeader("Retry-With-New-Token", "true")
                .build();
    }

    private String refresh(){
        try {
            String refreshToken = DataLocalManager.getRefreshToken();
            if (refreshToken == null){
                Log.d("lkhai4617", "refresh token null");
                return null;
            }
            LoginResponse loginResponse = LoginService.getInstance().refresh("refresh_token=" + refreshToken)
                    .subscribeOn(Schedulers.io())
                    .blockingGet();

            if (loginResponse != null && loginResponse.getData() != null) {
                Log.d("lkhai4617", "login response not null");
                String accessToken = loginResponse.getData().getAccessToken();

                if (accessToken != null) {
                    Log.d("lkhai4617", "access token new not null");
                    DataLocalManager.saveAccessToken(accessToken);
                    return accessToken;
                }
            }
            Log.d("lkhai4617", "login response null");
            return null;
        } catch (Exception ex) {
            Log.d("lkhai4617", ex.getMessage());
            return null;
        }
    }
}
