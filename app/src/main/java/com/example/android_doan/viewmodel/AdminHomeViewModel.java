package com.example.android_doan.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.android_doan.data.model.UserModel;
import com.example.android_doan.data.repository.RemoteRepository.AdminHomeRepository;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

public class AdminHomeViewModel extends ViewModel {
    private final MutableLiveData<UserModel> userLiveData = new MutableLiveData<>();
    private final CompositeDisposable disposables = new CompositeDisposable();
    private AdminHomeRepository adminHomeRepository;

    public AdminHomeViewModel(AdminHomeRepository adminHomeRepository) {
        this.adminHomeRepository = adminHomeRepository;
    }

    public MutableLiveData<UserModel> getUserLiveData() {
        return userLiveData;
    }

    public void getUser() {
        Disposable disposable = adminHomeRepository.getAccount()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retry(throwable -> {
                    if (throwable instanceof HttpException && ((HttpException) throwable).code() == 401) {
                        Thread.sleep(500);
                        return true;
                    }
                    return false;
                })
                .subscribe(response -> {
                    if (response != null && response.getData() != null && response.getData().getUserModel() != null) {
                        userLiveData.setValue(response.getData().getUserModel());
                    }
                }, throwable -> {
                    if (throwable.getMessage() != null) {
                        Log.d("lkhai4617", throwable.getMessage());
                    }
                });
        disposables.add(disposable);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }
}
