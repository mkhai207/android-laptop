package com.example.android_doan.data.repository.RemoteRepository;

import com.example.android_doan.data.api.user.ApiService;
import com.example.android_doan.data.model.response.OrderResponse;

import io.reactivex.Single;

public class OrderRepository {
    public Single<OrderResponse> getOrder(String userId, int page, int size){
        return ApiService.getInstance().getOrder(userId, page, size);
    }
}
