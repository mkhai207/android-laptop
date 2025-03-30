package com.example.android_doan.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.android_doan.data.model.request.AddToCartRequest;
import com.example.android_doan.data.model.response.GetCartResponse;
import com.example.android_doan.data.repository.RemoteRepository.CartRepository;
import com.example.android_doan.utils.Resource;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CartViewModel extends ViewModel {
    private CartRepository cartRepository;
    private MutableLiveData<List<GetCartResponse.Data>> itemCartLiveData = new MutableLiveData<>();

    private MutableLiveData<Integer> totalPriceCart = new MutableLiveData<>();

    private MutableLiveData<Resource> actionResult = new MutableLiveData<>();

    private CompositeDisposable disposables = new CompositeDisposable();


    public CartViewModel(CartRepository repository){
        this.cartRepository = repository;
    }

    public MutableLiveData<List<GetCartResponse.Data>> getItemCartLiveData(){
        return itemCartLiveData;
    }

    public MutableLiveData<Integer> getTotalPriceCart(){
        return totalPriceCart;
    }

    public MutableLiveData<Resource> getActionResult(){
        return actionResult;
    }

    public void getCart(){
        actionResult.setValue(Resource.loading());
        Disposable disposable = cartRepository.getCart()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response.getStatusCode() == 200 && response.getData() != null){
                        itemCartLiveData.setValue(response.getData());
                        int total = 0;
                        for (GetCartResponse.Data item: response.getData()){
                            total += item.getQuantity() * item.getProduct().getPrice();
                        }
                        totalPriceCart.setValue(total);

                        actionResult.setValue(Resource.success("Get carts success"));
                    } else {
                        actionResult.setValue(Resource.error("Get carts failure"));
                    }
                }, throwable -> {
                    if (throwable.getMessage() != null){
                        actionResult.setValue(Resource.error(throwable.getMessage()));
                    }
                });

        disposables.add(disposable);
    }

    public void updateQuantity(AddToCartRequest request){
        actionResult.setValue(Resource.loading());
        Disposable disposable = cartRepository.updateQuantity(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response.getStatusCode() == 200){
                        actionResult.setValue(Resource.success("Update quantity success"));
                        getCart();
                    } else {
                        actionResult.setValue(Resource.error("Update quantity failure"));
                        getCart();
                    }
                }, throwable -> {
                    if (throwable.getMessage() != null){
                        actionResult.setValue(Resource.error(throwable.getMessage()));
                        getCart();
                    }
                });
        disposables.add(disposable);
    }

    public void removeCart(String cartId){
        actionResult.setValue(Resource.loading());
        Disposable disposable = cartRepository.removeCart(cartId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response.getStatusCode() == 200){
                        actionResult.setValue(Resource.success("Remove success"));
                        getCart();
                    } else {
                        actionResult.setValue(Resource.error("Remove failure"));
                        getCart();
                    }
                }, throwable -> {
                    if (throwable.getMessage() != null){
                        actionResult.setValue(Resource.error(throwable.getMessage()));
                        getCart();
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
