package com.example.android_doan.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.android_doan.data.repository.RemoteRepository.ProductManagementRepository;

public class ProductManagementViewModelFactory implements ViewModelProvider.Factory {
    private ProductManagementRepository productManagementRepository;

    public ProductManagementViewModelFactory(ProductManagementRepository productManagementRepository) {
        this.productManagementRepository = productManagementRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ProductManagementViewModel.class)){
            return (T) new ProductManagementViewModel(productManagementRepository);
        } else {
            throw new IllegalArgumentException("Unknown ViewModel Class");
        }
    }
}
