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

    private MutableLiveData<Resource> actionResult = new MutableLiveData<>();

    private CompositeDisposable disposables = new CompositeDisposable();


    public CartViewModel(CartRepository repository){
        this.cartRepository = repository;
    }

    public MutableLiveData<List<GetCartResponse.Data>> getItemCartLiveData(){
        return itemCartLiveData;
    }

    public MutableLiveData<Resource> getActionResult(){
        return actionResult;
    }

    public void getCart(){
        actionResult.setValue(Resource.loading(Resource.Action.GET_CART));
        Disposable disposable = cartRepository.getCart()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response.getStatusCode() == 200 && response.getData() != null){
                        itemCartLiveData.setValue(response.getData());
                        actionResult.setValue(Resource.success("Get carts success", Resource.Action.GET_CART));
                    } else {
                        actionResult.setValue(Resource.error("Get carts failure", Resource.Action.GET_CART));
                    }
                }, throwable -> {
                    if (throwable.getMessage() != null){
                        actionResult.setValue(Resource.error(throwable.getMessage(), Resource.Action.GET_CART));
                    }
                });

        disposables.add(disposable);
    }

    public void updateQuantity(AddToCartRequest request){
        actionResult.setValue(Resource.loading(Resource.Action.UPDATE_QUANTITY));
        Disposable disposable = cartRepository.updateQuantity(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response.getStatusCode() == 200){
                        actionResult.setValue(Resource.success("Update quantity success", Resource.Action.UPDATE_QUANTITY));
                    } else {
                        actionResult.setValue(Resource.error("Update quantity failure", Resource.Action.UPDATE_QUANTITY));
                        getCart();
                    }
                }, throwable -> {
                    if (throwable.getMessage() != null){
                        actionResult.setValue(Resource.error(throwable.getMessage(), Resource.Action.UPDATE_QUANTITY));
                        getCart();
                    }
                });
        disposables.add(disposable);
    }

    public void removeCart(String cartId){
        actionResult.setValue(Resource.loading(Resource.Action.REMOVE_CART));
        Disposable disposable = cartRepository.removeCart(cartId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response.getStatusCode() == 200){
                        actionResult.setValue(Resource.success("Remove success", Resource.Action.REMOVE_CART));
                        getCart();
                    } else {
                        actionResult.setValue(Resource.error("Remove failure", Resource.Action.REMOVE_CART));
                        getCart();
                    }
                }, throwable -> {
                    if (throwable.getMessage() != null){
                        actionResult.setValue(Resource.error(throwable.getMessage(), Resource.Action.REMOVE_CART));
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
