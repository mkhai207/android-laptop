package com.example.android_doan.viewmodel;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.example.android_doan.data.api.user.ApiService;
import com.example.android_doan.data.model.request.AddToCartRequest;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ProductDetailViewModel extends ViewModel {
    private CompositeDisposable disposables = new CompositeDisposable();
    public void addToCart(AddToCartRequest request){
        Disposable disposable = ApiService.getInstance().addToCart(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response != null && response.getStatusCode() == 200){
                        Log.d("lkhai4617", "addToCart: success");
                    }
                }, throwable -> {
                    Log.d("lkhai4617", "addToCart: " + throwable.getMessage());
                });
        disposables.add(disposable);
    }

    @Override
    protected void onCleared() {
        disposables.clear();
        super.onCleared();
    }
}
