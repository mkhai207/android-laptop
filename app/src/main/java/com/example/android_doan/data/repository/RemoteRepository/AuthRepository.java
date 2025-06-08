package com.example.android_doan.data.repository.RemoteRepository;

import com.example.android_doan.base.BaseResponse;
import com.example.android_doan.data.api.login.LoginService;
import com.example.android_doan.data.api.user.ApiService;
import com.example.android_doan.data.model.UserModel;
import com.example.android_doan.data.model.request.LoginRequest;
import com.example.android_doan.data.model.request.RegisterRequest;
import com.example.android_doan.data.model.response.LoginResponse;

import io.reactivex.Single;


public class AuthRepository {
    public Single<LoginResponse> login(LoginRequest loginRequest) {
        return LoginService.getInstance().login(loginRequest);
    }

    public Single<BaseResponse<UserModel>> register(RegisterRequest registerRequest) {
        return LoginService.getInstance().register(registerRequest);
    }

    public Single<BaseResponse<String>> logout() {
        return ApiService.getInstance().logout();
    }
}
