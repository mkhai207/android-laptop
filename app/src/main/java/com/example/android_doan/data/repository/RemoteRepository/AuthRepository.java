package com.example.android_doan.data.repository.RemoteRepository;

import com.example.android_doan.data.api.login.LoginService;
import com.example.android_doan.data.model.request.LoginRequest;
import com.example.android_doan.data.model.response.LoginResponse;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class AuthRepository {
    public Single<LoginResponse> login(LoginRequest loginRequest){
        return LoginService.getInstance().login(loginRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
