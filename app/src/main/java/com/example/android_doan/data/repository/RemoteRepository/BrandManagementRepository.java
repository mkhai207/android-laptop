package com.example.android_doan.data.repository.RemoteRepository;

import com.example.android_doan.base.BasePagingResponse;
import com.example.android_doan.base.BaseResponse;
import com.example.android_doan.data.api.user.ApiService;
import com.example.android_doan.data.model.BrandModel;
import com.example.android_doan.data.model.request.CreateBrandRequest;

import io.reactivex.Single;

public class BrandManagementRepository {
    public Single<BaseResponse<BasePagingResponse<BrandModel>>> getBrand(int page, int size, String sort) {
        return ApiService.getInstance().getBrands(page, size, sort);
    }

    public Single<BaseResponse<BrandModel>> createBrand(CreateBrandRequest request) {
        return ApiService.getInstance().createBrand(request);
    }

    public Single<BaseResponse<String>> deleteBrand(int brandId) {
        return ApiService.getInstance().deleteBrand(brandId);
    }

    public Single<BaseResponse<BrandModel>> updateBrand(BrandModel request) {
        return ApiService.getInstance().updateBrand(request);
    }
}
