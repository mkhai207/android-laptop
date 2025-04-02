package com.example.android_doan.data.repository.RemoteRepository;

import com.example.android_doan.data.api.user.UserService;
import com.example.android_doan.data.model.response.BrandResponse;
import com.example.android_doan.data.model.response.ProductResponse;
import com.example.android_doan.data.model.response.AccountResponse;

import io.reactivex.Single;

public class HomeRepository {
    public Single<AccountResponse> getAccount(){
        return UserService.getInstance().getAccount();
    }
    public Single<ProductResponse> getProduct(int page, int size, String sort, String filter){
        if (filter == null || filter.isEmpty()){
            return UserService.getInstance().getAllProduct(page, size, sort, "");
        }
        return UserService.getInstance().getAllProduct(page, size, sort, filter);
    }
    public Single<BrandResponse> getBrands(){
        return UserService.getInstance().getBrands();
    }
}
