package com.example.android_doan.data.repository.RemoteRepository;

import com.example.android_doan.base.BaseResponse;
import com.example.android_doan.data.api.user.ApiService;
import com.example.android_doan.data.model.request.AddToCartRequest;
import com.example.android_doan.data.model.response.GetCartResponse;

import io.reactivex.Single;

public class CartRepository {
    public Single<GetCartResponse> getCart() {
        return ApiService.getInstance().getCart();
    }

    public Single<BaseResponse<String>> updateQuantity(AddToCartRequest request) {
        return ApiService.getInstance().updateQuantity(request);
    }

    public Single<BaseResponse<String>> removeCart(String cartId) {
        return ApiService.getInstance().removeCartResponse(cartId);
    }
}
