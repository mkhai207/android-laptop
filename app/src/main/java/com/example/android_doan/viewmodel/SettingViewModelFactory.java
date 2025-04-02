package com.example.android_doan.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.android_doan.data.repository.RemoteRepository.SettingRepository;

public class SettingViewModelFactory implements ViewModelProvider.Factory {
    private SettingRepository repository;

    public SettingViewModelFactory(SettingRepository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(SettingViewModel.class)){
            return (T) new SettingViewModel(repository);
        } else {
            throw new IllegalArgumentException("Unknown ViewModel Class");
        }
    }
}
