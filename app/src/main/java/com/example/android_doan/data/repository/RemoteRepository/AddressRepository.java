package com.example.android_doan.data.repository.RemoteRepository;

import com.example.android_doan.data.api.user.ApiService;
import com.example.android_doan.data.model.request.CreateAddressRequest;
import com.example.android_doan.data.model.response.AddressResponse;
import com.example.android_doan.data.model.response.CommonResponse;
import com.example.android_doan.data.model.response.CreateAddressResponse;
import com.example.android_doan.data.model.response.GetAddressResponse;

import io.reactivex.Single;

public class AddressRepository {
    public Single<CreateAddressResponse> createAddress(CreateAddressRequest request){
        return ApiService.getInstance().createAddress(request);
    }

    public Single<GetAddressResponse> getAddresses(String filter){
        return ApiService.getInstance().getAddresses(filter);
    }

    public Single<CreateAddressResponse> updateAddress(AddressResponse address){
        return ApiService.getInstance().updateAddress(address);
    }

    public Single<CommonResponse> deleteAddress(int addressId){
        return ApiService.getInstance().deleteAddress(addressId);
    }
}
