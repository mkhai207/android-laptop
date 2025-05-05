package com.example.android_doan.data.repository.RemoteRepository;

import com.example.android_doan.data.api.user.ApiService;
import com.example.android_doan.data.model.request.OrderRequest;
import com.example.android_doan.data.model.response.CommonResponse;
import com.example.android_doan.data.model.response.GetAddressResponse;
import com.example.android_doan.data.model.response.OrderResponse;

import io.reactivex.Single;

public class CheckoutRepository {
    public Single<OrderResponse> placeOrder(OrderRequest request){
        return ApiService.getInstance().placeOrder(request);
    }

    public Single<CommonResponse> clearCart(){
        return ApiService.getInstance().clearCart();
    }

    public Single<GetAddressResponse> getAddressDefault(String filter){
        return ApiService.getInstance().getAddresses(filter);
    }
}
