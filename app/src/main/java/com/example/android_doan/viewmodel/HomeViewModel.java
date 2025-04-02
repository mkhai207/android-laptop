package com.example.android_doan.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.android_doan.data.api.user.UserService;
import com.example.android_doan.data.model.response.BrandResponse;
import com.example.android_doan.data.model.response.Meta;
import com.example.android_doan.data.model.ProductModel;
import com.example.android_doan.data.model.UserModel;
import com.example.android_doan.data.repository.RemoteRepository.HomeRepository;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

public class HomeViewModel extends ViewModel {
    private HomeRepository homeRepository;

    private final MutableLiveData<UserModel> userLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<ProductModel>> productsLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoadingLiveData = new MutableLiveData<>(false);
    public MutableLiveData<BrandResponse.WrapperData> brandsLiveData = new MutableLiveData<>();
    public MutableLiveData<String> sortLiveData = new MutableLiveData<>();
    public MutableLiveData<String> filterLiveData = new MutableLiveData<>();
    private final CompositeDisposable disposables = new CompositeDisposable();
    private int currentPage = 0;
    private int pages = 1;
    private int pageSize = 5;
    private List<ProductModel> mListProduct = new ArrayList<>();

    public MutableLiveData<UserModel> getUserLiveData(){
        return userLiveData;
    }

    public MutableLiveData<List<ProductModel>> getProductsLiveData(){
        return productsLiveData;
    }

    public MutableLiveData<Boolean> getIsLoadingLiveData(){
        return isLoadingLiveData;
    }

    public MutableLiveData<BrandResponse.WrapperData> getBrandsLiveData(){
        return brandsLiveData;
    }

    public MutableLiveData<String> getSortLiveData(){
        return sortLiveData;
    }

    public MutableLiveData<String> getFilterLiveData(){
        return filterLiveData;
    }

    public HomeViewModel(HomeRepository repository){
        this.homeRepository = repository;
    }

    public void getUser(){
        Disposable disposable = homeRepository.getAccount()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retry(throwable -> {
                    if (throwable instanceof HttpException && ((HttpException) throwable).code() == 401){
                        Thread.sleep(500);
                        return true;
                    }
                    return false;
                })
                .subscribe(response -> {
                    if (response != null && response.getUser() != null && response.getUser().getUserModel() != null){
                        userLiveData.setValue(response.getUser().getUserModel());
                    }
                }, throwable -> {
                    if (throwable.getMessage() != null){
                        Log.d("lkhai4617", throwable.getMessage());
                    }
                });
        disposables.add(disposable);
    }

    public void loadProducts(int page){
        if (isLoadingLiveData.getValue() == Boolean.TRUE || page > pages){
            return;
        }
        isLoadingLiveData.setValue(true);
        String sort = sortLiveData.getValue();
        String filter = filterLiveData.getValue();
        Disposable disposable = homeRepository.getProduct(page, pageSize, sort, filter)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(productResponse -> {
                    if (productResponse != null){
                        Meta meta = productResponse.getData().getMeta();
                        if (meta != null){
                            currentPage = meta.getPage();
                            pageSize = meta.getPageSize();
                            pages = meta.getPages();
                        }
                        List<ProductModel> products = productResponse.getData().getProducts();
                        if (products != null){
                            mListProduct.addAll(products);
                            productsLiveData.setValue(mListProduct);
                            isLoadingLiveData.setValue(false);
                        }else {
//                            if (page == 1) {
//                                mListProduct.clear();
//                            }
//                            productsLiveData.setValue(mListProduct);
                        }
                        isLoadingLiveData.setValue(false);
                    }
                }, throwable -> {
                    isLoadingLiveData.setValue(false);
                    if (throwable.getMessage() != null){
                        Log.d("lkhai4617", throwable.getMessage());
                    }
                });
        disposables.add(disposable);
    }

    public void loadNextPage(){
        if (currentPage < pages){
            Log.d("lkhai4617", "load next page");
            loadProducts(currentPage + 1);
        }
    }

    public void getBrands(){
        Disposable disposable = homeRepository.getBrands()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response != null && response.getStatusCode() == 200){
                        brandsLiveData.setValue(response.getData());
                    }
                }, throwable -> {
                    if (throwable.getMessage() != null){

                    }
                });
        disposables.add(disposable);
    }

    public void resetAndLoad() {
        currentPage = 0;
        pages = 1;
        mListProduct.clear();
//        productsLiveData.setValue(new ArrayList<>(mListProduct));
        productsLiveData.setValue(mListProduct);
        loadNextPage();
    }

    @Override
    protected void onCleared() {
        disposables.clear();
        super.onCleared();
    }
}
