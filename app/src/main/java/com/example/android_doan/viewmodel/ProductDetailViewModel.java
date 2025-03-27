package com.example.android_doan.viewmodel;

import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;

import com.example.android_doan.data.api.user.UserService;
import com.example.android_doan.data.model.request.AddToCartRequest;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ProductDetailViewModel extends ViewModel {
    private CompositeDisposable disposables = new CompositeDisposable();
    public void addToCart(AddToCartRequest request, Context context){
        Disposable disposable = UserService.getInstance().addToCart(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response != null && response.getStatusCode() == 200){
                        Toast.makeText(context, "Add to cart success", Toast.LENGTH_LONG).show();
                    }
                }, throwable -> {
                    Toast.makeText(context, throwable.getMessage(), Toast.LENGTH_LONG).show();
                });
        disposables.add(disposable);
    }

    @Override
    protected void onCleared() {
        disposables.clear();
        super.onCleared();
    }
}
