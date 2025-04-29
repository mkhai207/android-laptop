package com.example.android_doan.data.repository.RemoteRepository;

import com.example.android_doan.data.api.user.ApiService;
import com.example.android_doan.data.model.BrandModel;
import com.example.android_doan.data.model.request.CreateBrandRequest;
import com.example.android_doan.data.model.response.BrandResponse;
import com.example.android_doan.data.model.response.CommonResponse;
import com.example.android_doan.data.model.response.CreateBrandResponse;

import io.reactivex.Single;

public class BrandManagementRepository {
    public Single<BrandResponse> getBrand(){
        return ApiService.getInstance().getBrands();
    }

    public Single<CreateBrandResponse> createBrand(CreateBrandRequest request){
        return ApiService.getInstance().createBrand(request);
    }

    public Single<CommonResponse> deleteBrand(int brandId){
        return ApiService.getInstance().deleteBrand(brandId);
    }

    public Single<CreateBrandResponse> updateBrand(BrandModel request){
        return ApiService.getInstance().updateBrand(request);
    }
}
