package com.example.android_doan.data.repository.RemoteRepository;

import com.example.android_doan.base.BasePagingResponse;
import com.example.android_doan.base.BaseResponse;
import com.example.android_doan.data.api.user.ApiService;
import com.example.android_doan.data.model.OrderData;

import io.reactivex.Single;

public class OrderRepository {
    public Single<BaseResponse<BasePagingResponse<OrderData>>> getOrder(String userId, int page, int size, String sort) {
        return ApiService.getInstance().getOrder(userId, page, size, sort);
    }
}
