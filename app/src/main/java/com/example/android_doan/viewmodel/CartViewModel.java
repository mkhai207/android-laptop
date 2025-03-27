package com.example.android_doan.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.android_doan.data.model.request.AddToCartRequest;
import com.example.android_doan.data.model.response.GetCartResponse;
import com.example.android_doan.data.repository.RemoteRepository.CartRepository;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CartViewModel extends ViewModel {
    private CartRepository cartRepository;
    private MutableLiveData<List<GetCartResponse.Data>> itemCartLiveData = new MutableLiveData<>();

    private CompositeDisposable disposables = new CompositeDisposable();


    public CartViewModel(CartRepository repository){
        this.cartRepository = repository;
    }

    public MutableLiveData<List<GetCartResponse.Data>> getItemCartLiveData(){
        return itemCartLiveData;
    }

    public void getCart(){
        Disposable disposable = cartRepository.getCart()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response.getStatusCode() == 200 && response.getData() != null){
                        itemCartLiveData.setValue(response.getData());
                    } else {
                        Log.d("lkhai4617", "getCart: null");
                    }
                }, throwable -> {
                    if (throwable.getMessage() != null){
                        Log.d("lkhai4617", throwable.getMessage());
                    }
                });

        disposables.add(disposable);
    }

    public void updateQuantity(AddToCartRequest request){
        Disposable disposable = cartRepository.updateQuantity(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response.getStatusCode() == 200){
                        Log.d("lkhai4617", "updateQuantity: success");
                    }
                }, throwable -> {
                    if (throwable.getMessage() != null){
                        Log.d("lkhai4617", throwable.getMessage());
                    }
                });
        disposables.add(disposable);
    }

    public void removeCart(String cartId){
        Disposable disposable = cartRepository.removeCart(cartId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response.getStatusCode() == 200){
                        Log.d("lkhai4617", "removeCart: success");
                    }
                }, throwable -> {
                    if (throwable.getMessage() != null){
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
