package com.example.android_doan.data.repository.RemoteRepository;

import com.example.android_doan.base.BaseResponse;
import com.example.android_doan.data.api.user.ApiService;
import com.example.android_doan.data.model.response.User;

import io.reactivex.Single;

public class ProfileRepository {
    public Single<BaseResponse<User>> getAccount() {
        return ApiService.getInstance().getAccount();
    }

    public Single<BaseResponse<String>> logout() {
        return ApiService.getInstance().logout();
    }
}
