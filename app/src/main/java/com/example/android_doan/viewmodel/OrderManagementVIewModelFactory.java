package com.example.android_doan.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.android_doan.data.repository.RemoteRepository.OrderManagementRepository;

public class OrderManagementVIewModelFactory implements ViewModelProvider.Factory {
    private OrderManagementRepository orderManagementRepository;

    public OrderManagementVIewModelFactory(OrderManagementRepository orderManagementRepository) {
        this.orderManagementRepository = orderManagementRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(OrderManagementViewModel.class)){
            return (T) new OrderManagementViewModel(orderManagementRepository);
        } else{
            throw new IllegalArgumentException("Unknown ViewModel Class");
        }
    }
}
