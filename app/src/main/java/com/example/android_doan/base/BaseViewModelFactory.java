package com.example.android_doan.base;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class BaseViewModelFactory<T> implements ViewModelProvider.Factory {
    private final T repository;
    private final Class<? extends ViewModel> viewModelClass;

    public BaseViewModelFactory(T repository, Class<? extends ViewModel> viewModelClass) {
        this.repository = repository;
        this.viewModelClass = viewModelClass;
    }

    @NonNull
    @Override
    public <V extends ViewModel> V create(@NonNull Class<V> modelClass) {
        if (modelClass.isAssignableFrom(viewModelClass)) {
            try {
                return (V) viewModelClass.getConstructor(repository.getClass()).newInstance(repository);
            } catch (Exception e) {
                throw new IllegalArgumentException("Error creating ViewModel instance", e);
            }
        }
        throw new IllegalArgumentException("Unknown ViewModel Class: " + modelClass.getName());
    }
}
