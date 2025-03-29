package com.example.android_doan.data.repository.RemoteRepository;

import com.example.android_doan.data.api.user.UserService;
import com.example.android_doan.data.model.request.OrderRequest;
import com.example.android_doan.data.model.response.CommonResponse;
import com.example.android_doan.data.model.response.OrderResponse;

import io.reactivex.Single;

public class CheckoutRepository {
    public Single<OrderResponse> placeOrder(OrderRequest request){
        return UserService.getInstance().placeOrder(request);
    }

    public Single<CommonResponse> clearCart(){
        return UserService.getInstance().clearCart();
    }
}
