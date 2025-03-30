package com.example.android_doan.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.android_doan.data.repository.RemoteRepository.ProfileRepository;

public class ProfileViewModelFactory implements ViewModelProvider.Factory {
    private ProfileRepository repository;

    public ProfileViewModelFactory(ProfileRepository repository){
        this.repository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ProfileViewModel.class)){
            return (T) new ProfileViewModel(repository);
        } else {
            throw new IllegalArgumentException("Unknown ViewModel Class");
        }
    }
}
