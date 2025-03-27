package com.example.android_doan.viewmodel;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.android_doan.data.repository.RemoteRepository.AuthRepository;

public class AuthViewModelFactory implements ViewModelProvider.Factory {
    private Context context;
    private AuthRepository authRepository;

    public AuthViewModelFactory(Context context, AuthRepository authRepository){
        this.context = context;
        this.authRepository = authRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(AuthViewModel.class)){
            return (T) new AuthViewModel(context, authRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel Class");
    }
}
