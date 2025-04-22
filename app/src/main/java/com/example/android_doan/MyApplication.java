package com.example.android_doan;

import android.app.Application;

import com.example.android_doan.data.repository.LocalRepository.DataLocalManager;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DataLocalManager.init(getApplicationContext());
    }
}
