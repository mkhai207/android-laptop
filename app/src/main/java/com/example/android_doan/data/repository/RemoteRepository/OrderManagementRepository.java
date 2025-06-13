package com.example.android_doan.data.repository.RemoteRepository;

import com.example.android_doan.base.BasePagingResponse;
import com.example.android_doan.base.BaseResponse;
import com.example.android_doan.data.api.user.ApiService;
import com.example.android_doan.data.model.UserModel;
import com.example.android_doan.data.model.request.CreateAdminOrderRequest;
import com.example.android_doan.data.model.request.UpdateOrderRequest;
import com.example.android_doan.data.model.response.OrderAdminResponse;

import io.reactivex.Single;

public class OrderManagementRepository {
    public Single<BaseResponse<BasePagingResponse<OrderAdminResponse>>> getAllOrder(String filter) {
        return ApiService.getInstance().getAllOrder(filter);
    }

    public Single<BaseResponse<OrderAdminResponse>> updateOrder(UpdateOrderRequest request) {
        return ApiService.getInstance().updateOrder(request);
    }

    public Single<BaseResponse<BasePagingResponse<UserModel>>> getAllUser(int page, int size, String sort) {
        return ApiService.getInstance().getAllUser(page, size, sort);
    }

    public Single<BaseResponse<OrderAdminResponse>> createOrder(CreateAdminOrderRequest request) {
        return ApiService.getInstance().createOrder(request);
    }

}
