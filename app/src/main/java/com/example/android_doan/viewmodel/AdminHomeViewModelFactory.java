package com.example.android_doan.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.android_doan.data.repository.RemoteRepository.AdminHomeRepository;

public class AdminHomeViewModelFactory implements ViewModelProvider.Factory{
    private AdminHomeRepository adminHomeRepository;

    public AdminHomeViewModelFactory(AdminHomeRepository adminHomeRepository) {
        this.adminHomeRepository = adminHomeRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(AdminHomeViewModel.class)){
            return (T) new AdminHomeViewModel(adminHomeRepository);
        } else {
            throw new IllegalArgumentException("Unknown ViewModel Class");
        }
    }
}
