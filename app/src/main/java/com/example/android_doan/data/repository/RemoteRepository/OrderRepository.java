package com.example.android_doan.data.repository.RemoteRepository;

import com.example.android_doan.data.api.user.UserService;
import com.example.android_doan.data.model.response.OrderResponse;

import io.reactivex.Single;

public class OrderRepository {
    public Single<OrderResponse> getOrder(String userId){
        return UserService.getInstance().getOrder(userId);
    }
}
