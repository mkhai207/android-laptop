package com.example.android_doan.data.repository.RemoteRepository;

import com.example.android_doan.base.BaseResponse;
import com.example.android_doan.data.api.user.ApiService;
import com.example.android_doan.data.model.ProductModel;
import com.example.android_doan.data.model.request.AddToCartRequest;

import io.reactivex.Single;

public class ProductDetailRepository {
    public Single<BaseResponse<String>> addToCart(AddToCartRequest request) {
        return ApiService.getInstance().addToCart(request);
    }

    public Single<BaseResponse<ProductModel>> getProduct(String productId) {
        return ApiService.getInstance().getProduct(productId);
    }
}
