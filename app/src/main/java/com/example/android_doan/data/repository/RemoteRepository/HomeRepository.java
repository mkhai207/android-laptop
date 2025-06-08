package com.example.android_doan.data.repository.RemoteRepository;

import com.example.android_doan.base.BasePagingResponse;
import com.example.android_doan.base.BaseResponse;
import com.example.android_doan.data.api.user.ApiService;
import com.example.android_doan.data.model.BrandModel;
import com.example.android_doan.data.model.ProductModel;
import com.example.android_doan.data.model.response.User;

import io.reactivex.Single;

public class HomeRepository {
    public Single<BaseResponse<User>> getAccount() {
        return ApiService.getInstance().getAccount();
    }

    public Single<BaseResponse<BasePagingResponse<ProductModel>>> getProduct(int page, int size, String sort, String filter) {
        if (filter == null || filter.isEmpty()) {
            return ApiService.getInstance().getAllProduct(page, size, sort, "");
        }
        return ApiService.getInstance().getAllProduct(page, size, sort, filter);
    }

    public Single<BaseResponse<BasePagingResponse<BrandModel>>> getBrands() {
        return ApiService.getInstance().getBrands();
    }
}
