package com.example.android_doan.data.repository.RemoteRepository;

import com.example.android_doan.data.api.user.ApiService;
import com.example.android_doan.data.model.request.AddToCartRequest;
import com.example.android_doan.data.model.response.GetCartResponse;
import com.example.android_doan.data.model.response.CommonResponse;

import io.reactivex.Single;

public class CartRepository {
    public Single<GetCartResponse> getCart(){
        return ApiService.getInstance().getCart();
    }

    public Single<CommonResponse> updateQuantity(AddToCartRequest request){
        return ApiService.getInstance().updateQuantity(request);
    }

    public Single<CommonResponse> removeCart(String cartId){
        return ApiService.getInstance().removeCartResponse(cartId);
    }
}
