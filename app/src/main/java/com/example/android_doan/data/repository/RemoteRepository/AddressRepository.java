package com.example.android_doan.data.repository.RemoteRepository;

import com.example.android_doan.base.BasePagingResponse;
import com.example.android_doan.base.BaseResponse;
import com.example.android_doan.data.api.user.ApiService;
import com.example.android_doan.data.model.request.CreateAddressRequest;
import com.example.android_doan.data.model.response.AddressResponse;

import io.reactivex.Single;

public class AddressRepository {
    public Single<BaseResponse<AddressResponse>> createAddress(CreateAddressRequest request) {
        return ApiService.getInstance().createAddress(request);
    }

    public Single<BaseResponse<BasePagingResponse<AddressResponse>>> getAddresses(String filter) {
        return ApiService.getInstance().getAddresses(filter);
    }

    public Single<BaseResponse<AddressResponse>> updateAddress(AddressResponse address) {
        return ApiService.getInstance().updateAddress(address);
    }

    public Single<BaseResponse<String>> deleteAddress(int addressId) {
        return ApiService.getInstance().deleteAddress(addressId);
    }
}
