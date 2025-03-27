package com.example.android_doan.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.android_doan.data.repository.RemoteRepository.CartRepository;

public class CartViewModeFactory implements ViewModelProvider.Factory {
    private CartRepository cartRepository;

    public CartViewModeFactory(CartRepository repository){
        this.cartRepository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(CartViewModel.class)){
            return (T) new CartViewModel(cartRepository);
        } else {
            throw new IllegalArgumentException("Unknown CartViewModel Class");
        }
    }
}
