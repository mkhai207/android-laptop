package com.example.android_doan.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.android_doan.data.model.Address;
import com.example.android_doan.data.model.request.OrderRequest;
import com.example.android_doan.data.repository.RemoteRepository.CheckoutRepository;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CheckoutViewModel extends ViewModel {
    private CheckoutRepository repository;
    private MutableLiveData<Address> address = new MutableLiveData<>();
    private CompositeDisposable disposables = new CompositeDisposable();

    public interface IActionResultCallback{
        void actionResultCallback(boolean isSuccess);
    }

    public CheckoutViewModel(CheckoutRepository repository){
        this.repository = repository;
    }
    public MutableLiveData<Address> getAddress(){
        return address;
    }

    public void setAddress(Address newAddress){
        address.setValue(newAddress);
    }

    public void placeOrder(OrderRequest request, IActionResultCallback callback){
        Disposable disposable = repository.placeOrder(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response != null && response.getStatusCode() == 201){
                        callback.actionResultCallback(true);
                    } else {
                        callback.actionResultCallback(false);
                    }
                }, throwable -> {
                    if (throwable.getMessage() != null){
                        Log.d("lkhai4617", throwable.getMessage());
                        callback.actionResultCallback(false);
                    }
                });
        disposables.add(disposable);
    }

    public void clearCart(IActionResultCallback callback){
        Disposable disposable = repository.clearCart()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response != null && response.getStatusCode() == 200){
                        callback.actionResultCallback(true);
                    }else {
                        callback.actionResultCallback(false);
                    }
                }, throwable -> {
                    if (throwable.getMessage() != null){
                        Log.d("lkhai4617", throwable.getMessage());
                        callback.actionResultCallback(false);
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
