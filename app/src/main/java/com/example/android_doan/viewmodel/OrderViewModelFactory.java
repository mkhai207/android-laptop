package com.example.android_doan.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.android_doan.data.repository.RemoteRepository.OrderRepository;

public class OrderViewModelFactory implements ViewModelProvider.Factory {
    private OrderRepository repository;

    public OrderViewModelFactory(OrderRepository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(OrderViewModel.class)){
            return (T) new OrderViewModel(repository);
        } else{
            throw new IllegalArgumentException("Unknown ViewModel Class");
        }
    }
}
