package com.example.android_doan.data.repository.RemoteRepository;

import com.example.android_doan.base.BaseResponse;
import com.example.android_doan.data.api.user.ApiService;
import com.example.android_doan.data.model.response.User;

import io.reactivex.Single;

public class AdminHomeRepository {
    public Single<BaseResponse<User>> getAccount() {
        return ApiService.getInstance().getAccount();
    }
}
