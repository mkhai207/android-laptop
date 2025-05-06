package com.example.android_doan.data.repository.RemoteRepository;

import com.example.android_doan.data.api.user.ApiService;
import com.example.android_doan.data.model.request.UpdateOrderRequest;
import com.example.android_doan.data.model.response.CommonResponse;
import com.example.android_doan.data.model.response.GetAllOrderResponse;
import com.example.android_doan.data.model.response.UpdateOrderResponse;

import io.reactivex.Single;

public class OrderManagementRepository {
    public Single<GetAllOrderResponse> getAllOrder(String filter){
        return ApiService.getInstance().getAllOrder(filter);
    }

    public Single<UpdateOrderResponse> updateOrder(UpdateOrderRequest request){
        return ApiService.getInstance().updateOrder(request);
    }
}
