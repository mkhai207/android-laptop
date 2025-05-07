package com.example.android_doan.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.android_doan.data.repository.RemoteRepository.ProductDetailRepository;

public class ProductDetailViewModelFactory implements ViewModelProvider.Factory {
    private ProductDetailRepository productDetailRepository;

    public ProductDetailViewModelFactory(ProductDetailRepository productDetailRepository) {
        this.productDetailRepository = productDetailRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ProductDetailViewModel.class)){
            return (T) new ProductDetailViewModel(productDetailRepository);
        } else {
            throw new IllegalArgumentException("Unknown ViewModel Class");
        }
    }
}
