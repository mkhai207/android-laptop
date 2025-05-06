package com.example.android_doan.data.model.request;

import com.example.android_doan.data.model.response.AddressResponse;

public class UpdateOrderRequest {
    private int id;
    private String status;
    private AddressResponse address;

    public UpdateOrderRequest(int id, String status, AddressResponse address) {
        this.id = id;
        this.status = status;
        this.address = address;
    }
}
