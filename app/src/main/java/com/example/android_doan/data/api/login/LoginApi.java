package com.example.android_doan.data.api.login;

import com.example.android_doan.base.BaseResponse;
import com.example.android_doan.data.model.UserModel;
import com.example.android_doan.data.model.request.LoginRequest;
import com.example.android_doan.data.model.request.RegisterRequest;
import com.example.android_doan.data.model.response.LoginResponse;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface LoginApi {
    @POST("api/v1/auth/login")
    Single<LoginResponse> login(@Body LoginRequest loginRequest);

    @POST("api/v1/auth/register")
    Single<BaseResponse<UserModel>> register(@Body RegisterRequest registerRequest);

    @GET("api/v1/auth/refresh")
    Single<LoginResponse> refresh(@Header("Cookie") String refreshToken);

}
