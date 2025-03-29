package com.example.android_doan.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.android_doan.data.repository.RemoteRepository.CheckoutRepository;

public class CheckoutViewModelFactory implements ViewModelProvider.Factory {
    private CheckoutRepository repository;

    public CheckoutViewModelFactory(CheckoutRepository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(CheckoutViewModel.class)){
            return (T) new  CheckoutViewModel(repository);
        } else {
            throw new IllegalArgumentException("Unknown ViewModel Class");
        }
    }
}
