package com.example.android_doan.data.repository.RemoteRepository;

import com.example.android_doan.data.api.user.UserService;
import com.example.android_doan.data.model.response.UserResponse;

import io.reactivex.Single;

public class HomeRepository {
    public Single<UserResponse> getAccount(){
        return UserService.getInstance().getAccount();
    }

}
