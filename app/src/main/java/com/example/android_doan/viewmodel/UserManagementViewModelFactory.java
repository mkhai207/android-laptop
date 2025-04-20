package com.example.android_doan.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.android_doan.data.repository.RemoteRepository.UserManagementRepository;

public class UserManagementViewModelFactory implements ViewModelProvider.Factory {
    private UserManagementRepository userManagementRepository;

    public UserManagementViewModelFactory(UserManagementRepository userManagementRepository) {
        this.userManagementRepository = userManagementRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(UserManagementViewModel.class)){
            return (T) new UserManagementViewModel(userManagementRepository);
        } else {
            throw new IllegalArgumentException("Unknown ViewModel Class");
        }
    }
}
