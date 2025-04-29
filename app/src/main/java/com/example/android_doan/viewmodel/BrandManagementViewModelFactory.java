package com.example.android_doan.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.android_doan.data.repository.RemoteRepository.BrandManagementRepository;

public class BrandManagementViewModelFactory implements ViewModelProvider.Factory {
    private BrandManagementRepository brandManagementRepository;
    public BrandManagementViewModelFactory(BrandManagementRepository brandManagementRepository) {
        this.brandManagementRepository = brandManagementRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(BrandManagementViewModel.class)){
            return (T) new BrandManagementViewModel(brandManagementRepository);
        } else {
            throw new IllegalArgumentException("Unknown ViewModel Class");
        }
    }
}
