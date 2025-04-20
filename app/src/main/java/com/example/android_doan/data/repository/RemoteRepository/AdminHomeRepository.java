package com.example.android_doan.data.repository.RemoteRepository;

import com.example.android_doan.data.api.user.ApiService;
import com.example.android_doan.data.model.response.AccountResponse;

import io.reactivex.Single;

public class AdminHomeRepository {
    public Single<AccountResponse> getAccount(){
        return ApiService.getInstance().getAccount();
    }
}
