package com.example.android_doan.data.repository.RemoteRepository;

import com.example.android_doan.data.api.user.ApiService;
import com.example.android_doan.data.model.request.AddToCartRequest;
import com.example.android_doan.data.model.response.AddToCartResponse;
import com.example.android_doan.data.model.response.GetProductResponse;

import io.reactivex.Single;

public class ProductDetailRepository {
    public Single<AddToCartResponse> addToCart(AddToCartRequest request){
        return ApiService.getInstance().addToCart(request);
    }

    public Single<GetProductResponse> getProduct(String productId){
        return ApiService.getInstance().getProduct(productId);
    }
}
