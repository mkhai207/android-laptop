package com.example.android_doan.data.repository.RemoteRepository;

import com.example.android_doan.data.api.user.UserService;
import com.example.android_doan.data.model.response.CommonResponse;
import com.example.android_doan.data.model.response.AccountResponse;

import io.reactivex.Single;

public class ProfileRepository {
    public Single<AccountResponse> getAccount(){
        return UserService.getInstance().getAccount();
    }

    public Single<CommonResponse> logout(){
        return UserService.getInstance().logout();
    }
}
