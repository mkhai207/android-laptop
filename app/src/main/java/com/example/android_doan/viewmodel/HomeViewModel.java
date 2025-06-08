package com.example.android_doan.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.android_doan.base.BasePagingResponse;
import com.example.android_doan.data.model.BrandModel;
import com.example.android_doan.data.model.ProductModel;
import com.example.android_doan.data.model.UserModel;
import com.example.android_doan.data.model.response.Meta;
import com.example.android_doan.data.repository.RemoteRepository.HomeRepository;
import com.example.android_doan.utils.Resource;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

public class HomeViewModel extends ViewModel {
    private final MutableLiveData<UserModel> userLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<ProductModel>> productsLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoadingLiveData = new MutableLiveData<>(false);
    private final CompositeDisposable disposables = new CompositeDisposable();
    public MutableLiveData<BasePagingResponse<BrandModel>> brandsLiveData = new MutableLiveData<>();
    public MutableLiveData<Resource> apiResultLiveData = new MutableLiveData<>();
    public MutableLiveData<String> sortLiveData = new MutableLiveData<>();
    public MutableLiveData<String> filterLiveData = new MutableLiveData<>();
    private HomeRepository homeRepository;
    private int currentPage = 0;
    private int pages = 1;
    private int pageSize = 5;
    private List<ProductModel> mListProduct = new ArrayList<>();

    public HomeViewModel(HomeRepository repository) {
        this.homeRepository = repository;
    }

    public MutableLiveData<UserModel> getUserLiveData() {
        return userLiveData;
    }

    public MutableLiveData<List<ProductModel>> getProductsLiveData() {
        return productsLiveData;
    }

    public MutableLiveData<Boolean> getIsLoadingLiveData() {
        return isLoadingLiveData;
    }

    public MutableLiveData<BasePagingResponse<BrandModel>> getBrandsLiveData() {
        return brandsLiveData;
    }

    public MutableLiveData<Resource> getApiResultLiveData() {
        return apiResultLiveData;
    }

    public MutableLiveData<String> getSortLiveData() {
        return sortLiveData;
    }

    public MutableLiveData<String> getFilterLiveData() {
        return filterLiveData;
    }

    public void getUser() {
        Disposable disposable = homeRepository.getAccount()
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
                    if (throwable instanceof HttpException) {
                        HttpException httpException = (HttpException) throwable;
                        ResponseBody errorBody = Objects.requireNonNull(httpException.response()).errorBody();
                        if (errorBody != null) {
                            try {
                                String errorJson = errorBody.string();
                                JSONObject jsonObject = new JSONObject(errorJson);
                                String errorMessage = jsonObject.optString("message", "Call api failed");
                                apiResultLiveData.setValue(Resource.error(errorMessage));
                            } catch (Exception e) {
                                apiResultLiveData.setValue(Resource.error("Unknown error"));
                            }
                        } else {
                            apiResultLiveData.setValue(Resource.error("Unknown server error"));
                        }
                    } else {
                        apiResultLiveData.setValue(Resource.error(throwable.getMessage()));
                    }
                });
        disposables.add(disposable);
    }

    public void loadProducts(int page) {
        if (isLoadingLiveData.getValue() == Boolean.TRUE || page > pages) {
            return;
        }
        isLoadingLiveData.setValue(true);
        String sort = sortLiveData.getValue();
        String filter = filterLiveData.getValue();
        Disposable disposable = homeRepository.getProduct(page, pageSize, sort, filter)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(productResponse -> {
                    if (productResponse != null) {
                        Meta meta = productResponse.getData().getMeta();
                        if (meta != null) {
                            currentPage = meta.getPage();
                            pageSize = meta.getPageSize();
                            pages = meta.getPages();
                        }
                        List<ProductModel> products = productResponse.getData().getResult();
                        if (products != null) {
                            mListProduct.addAll(products);
                            productsLiveData.setValue(mListProduct);
                            isLoadingLiveData.setValue(false);
                        } else {
//                            if (page == 1) {
//                                mListProduct.clear();
//                            }
//                            productsLiveData.setValue(mListProduct);
                        }
                        isLoadingLiveData.setValue(false);
                    }
                }, throwable -> {
                    isLoadingLiveData.setValue(false);
                    if (throwable instanceof HttpException) {
                        HttpException httpException = (HttpException) throwable;
                        ResponseBody errorBody = Objects.requireNonNull(httpException.response()).errorBody();
                        if (errorBody != null) {
                            try {
                                String errorJson = errorBody.string();
                                JSONObject jsonObject = new JSONObject(errorJson);
                                String errorMessage = jsonObject.optString("message", "Call api failed");
                                apiResultLiveData.setValue(Resource.error(errorMessage));
                            } catch (Exception e) {
                                apiResultLiveData.setValue(Resource.error("Unknown error"));
                            }
                        } else {
                            apiResultLiveData.setValue(Resource.error("Unknown server error"));
                        }
                    } else {
                        apiResultLiveData.setValue(Resource.error(throwable.getMessage()));
                    }
                });
        disposables.add(disposable);
    }

    public void loadNextPage() {
        if (currentPage < pages) {
            Log.d("lkhai4617", "load next page");
            loadProducts(currentPage + 1);
        }
    }

    public void getBrands() {
        Disposable disposable = homeRepository.getBrands()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response != null && response.getStatusCode() == 200) {
                        brandsLiveData.setValue(response.getData());
                    }
                }, throwable -> {
                    if (throwable instanceof HttpException) {
                        HttpException httpException = (HttpException) throwable;
                        ResponseBody errorBody = Objects.requireNonNull(httpException.response()).errorBody();
                        if (errorBody != null) {
                            try {
                                String errorJson = errorBody.string();
                                JSONObject jsonObject = new JSONObject(errorJson);
                                String errorMessage = jsonObject.optString("message", "Call api failed");
                                apiResultLiveData.setValue(Resource.error(errorMessage));
                            } catch (Exception e) {
                                apiResultLiveData.setValue(Resource.error("Unknown error"));
                            }
                        } else {
                            apiResultLiveData.setValue(Resource.error("Unknown server error"));
                        }
                    } else {
                        apiResultLiveData.setValue(Resource.error(throwable.getMessage()));
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
