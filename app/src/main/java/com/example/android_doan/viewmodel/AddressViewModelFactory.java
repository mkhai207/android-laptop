package com.example.android_doan.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.android_doan.data.repository.RemoteRepository.AddressRepository;

public class AddressViewModelFactory implements ViewModelProvider.Factory {
    private AddressRepository addressRepository;

    public AddressViewModelFactory(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(AddressViewModel.class)){
            return (T) new AddressViewModel(addressRepository);
        } else {
            throw new IllegalArgumentException("Unknown ViewModel Class");
        }
    }
}
