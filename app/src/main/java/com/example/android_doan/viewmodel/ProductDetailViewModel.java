package com.example.android_doan.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.android_doan.data.api.user.ApiService;
import com.example.android_doan.data.model.ProductModel;
import com.example.android_doan.data.model.request.AddToCartRequest;
import com.example.android_doan.data.repository.RemoteRepository.ProductDetailRepository;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ProductDetailViewModel extends ViewModel {
    private ProductDetailRepository productDetailRepository;
    private CompositeDisposable disposables = new CompositeDisposable();
    private MutableLiveData<ProductModel> productLiveData = new MutableLiveData<>();
    public MutableLiveData<ProductModel> getProductLiveData(){
        return productLiveData;
    }
    public ProductDetailViewModel(ProductDetailRepository productDetailRepository) {
        this.productDetailRepository = productDetailRepository;
    }
    public void addToCart(AddToCartRequest request){
        Disposable disposable = productDetailRepository.addToCart(request)
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

    public void getProduct(String productId){
        Disposable disposable = productDetailRepository.getProduct(productId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response != null && response.getStatusCode() == 200){
                        productLiveData.setValue(response.getData());
                    }
                }, throwable -> {
                    Log.d("lkhai4617", "getProduct: " + throwable.getMessage());
                });
        disposables.add(disposable);
    }

    @Override
    protected void onCleared() {
        disposables.clear();
        super.onCleared();
    }
}
