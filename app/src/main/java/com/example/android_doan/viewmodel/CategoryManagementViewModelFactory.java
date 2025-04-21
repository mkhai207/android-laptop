package com.example.android_doan.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.android_doan.data.repository.RemoteRepository.CategoryManagementRepository;

public class CategoryManagementViewModelFactory implements ViewModelProvider.Factory {
    private CategoryManagementRepository categoryManagementRepository;

    public CategoryManagementViewModelFactory(CategoryManagementRepository categoryManagementRepository) {
        this.categoryManagementRepository = categoryManagementRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(CategoryManagementViewModel.class)){
            return (T) new CategoryManagementViewModel(categoryManagementRepository);
        } else {
            throw new IllegalArgumentException("Unknown ViewModel Class");
        }
    }
}
