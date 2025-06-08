package com.example.android_doan.data.repository.RemoteRepository;

import com.example.android_doan.base.BasePagingResponse;
import com.example.android_doan.base.BaseResponse;
import com.example.android_doan.data.api.user.ApiService;
import com.example.android_doan.data.model.OrderData;
import com.example.android_doan.data.model.request.OrderRequest;
import com.example.android_doan.data.model.response.AddressResponse;

import io.reactivex.Single;

public class CheckoutRepository {
    public Single<BaseResponse<BasePagingResponse<OrderData>>> placeOrder(OrderRequest request) {
        return ApiService.getInstance().placeOrder(request);
    }

    public Single<BaseResponse<String>> clearCart() {
        return ApiService.getInstance().clearCart();
    }

    public Single<BaseResponse<BasePagingResponse<AddressResponse>>> getAddressDefault(String filter) {
        return ApiService.getInstance().getAddresses(filter);
    }
}
