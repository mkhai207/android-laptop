package com.example.android_doan.data.repository.RemoteRepository;

import com.example.android_doan.data.api.user.ApiService;
import com.example.android_doan.data.model.response.CommonResponse;
import com.example.android_doan.data.model.response.AccountResponse;

import io.reactivex.Single;

public class ProfileRepository {
    public Single<AccountResponse> getAccount(){
        return ApiService.getInstance().getAccount();
    }

    public Single<CommonResponse> logout(){
        return ApiService.getInstance().logout();
    }
}
