package com.example.android_doan.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.android_doan.data.model.BrandModel;
import com.example.android_doan.data.model.CategoryModel;
import com.example.android_doan.data.model.ProductModel;
import com.example.android_doan.data.model.request.CreateProductRequest;
import com.example.android_doan.data.model.request.UpdateProductRequest;
import com.example.android_doan.data.model.response.BrandResponse;
import com.example.android_doan.data.model.response.Meta;
import com.example.android_doan.data.model.response.UploadFileResponse;
import com.example.android_doan.data.repository.RemoteRepository.ProductManagementRepository;
import com.example.android_doan.utils.Resource;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ProductManagementViewModel extends ViewModel {
    private ProductManagementRepository productManagementRepository;
    private MutableLiveData<List<ProductModel>> productsLiveData = new MutableLiveData<>();
    public MutableLiveData<List<BrandModel>> brandsLiveData = new MutableLiveData<>();
    public MutableLiveData<List<CategoryModel>> categoriesLiveData = new MutableLiveData<>();
    private MutableLiveData<UploadFileResponse> fileLiveData = new MutableLiveData<>();
    private MutableLiveData<Resource> apiResultLiveData = new MutableLiveData<>(new Resource(Resource.Status.SUCCESS, ""));
    private final CompositeDisposable disposables = new CompositeDisposable();
    private int currentPage = 0;
    private int pages = 1;
    private int pageSize = 10;
    private List<ProductModel> mListProduct = new ArrayList<>();

    public MutableLiveData<List<ProductModel>> getProductsLiveData(){
        return productsLiveData;
    }
    public MutableLiveData<UploadFileResponse> getFileLiveData(){
        return fileLiveData;
    }

    public MutableLiveData<List<BrandModel>> getBrandsLiveData(){
        return brandsLiveData;
    }

    public MutableLiveData<List<CategoryModel>> getCategoriesLiveData(){
        return categoriesLiveData;
    }


    public MutableLiveData<Resource> getApiResultLiveData(){
        return apiResultLiveData;
    }

    public ProductManagementViewModel(ProductManagementRepository productManagementRepository) {
        this.productManagementRepository = productManagementRepository;
    }

    public void getAllProduct(int page){
        if (apiResultLiveData.getValue().getStatus() == Resource.Status.LOADING || page > pages){
            return;
        }
        apiResultLiveData.setValue(Resource.loading());
        String sort = "";
        String filter = "";
        Disposable disposable = productManagementRepository.getAllProduct(page, pageSize, sort, filter)
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
                            apiResultLiveData.setValue(Resource.success("getAllProduct"));
                        }
                    } else {
                        apiResultLiveData.setValue(Resource.error("getAllProduct"));
                    }
                }, throwable -> {
                    if (throwable.getMessage() != null){
                        apiResultLiveData.setValue(Resource.error(throwable.getMessage()));
                    }
                });
        disposables.add(disposable);
    }

    public void loadNextPage(){
        if (currentPage < pages){
            Log.d("lkhai4617", "load next page");
            getAllProduct(currentPage + 1);
        }
    }

    public void refresh() {
        currentPage = 0;
        mListProduct.clear();
        productsLiveData.setValue(mListProduct);
        loadNextPage();
    }

    public void getBrands(){
        apiResultLiveData.setValue(Resource.loading());
        Disposable disposable = productManagementRepository.getAllBrand()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response != null && response.getStatusCode() == 200){
                        brandsLiveData.setValue(response.getData().getResult());
                        apiResultLiveData.setValue(Resource.success("getBrands"));
                    }
                }, throwable -> {
                    if (throwable.getMessage() != null){
                        apiResultLiveData.setValue(Resource.error(throwable.getMessage()));
                    }
                });
        disposables.add(disposable);
    }

    public void getAllCategory(){
        apiResultLiveData.setValue(Resource.loading());
        Disposable disposable = productManagementRepository.getAllCategories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response != null && response.getStatusCode() == 200){
                        categoriesLiveData.setValue(response.getData().getResult());
                        apiResultLiveData.setValue(Resource.success("getAllCategory"));
                    }
                }, throwable -> {
                    if (throwable.getMessage() != null){
                        apiResultLiveData.setValue(Resource.error(throwable.getMessage()));
                    }
                });
        disposables.add(disposable);
    }

    public void uploadFile(RequestBody folder, MultipartBody.Part file){
        apiResultLiveData.setValue(Resource.loading());
        Disposable disposable = productManagementRepository.uploadFile(folder, file)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response != null && response.getStatusCode() == 200){
                        fileLiveData.setValue(response);
                        apiResultLiveData.setValue(Resource.success("uploadFile"));
                    } else {
                        apiResultLiveData.setValue(Resource.error("uploadFile"));
                    }
                }, throwable -> {
                    if (throwable.getMessage() != null){
                        apiResultLiveData.setValue(Resource.error(throwable.getMessage()));
                    }
                });
        disposables.add(disposable);
    }

    public void updateProduct(UpdateProductRequest request){
        apiResultLiveData.setValue(Resource.loading());
        Disposable disposable = productManagementRepository.updateProduct(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response != null && response.getStatusCode() == 200){
                        apiResultLiveData.setValue(Resource.success("updateProduct"));
                    } else {
                        apiResultLiveData.setValue(Resource.error("updateProduct"));
                    }
                }, throwable -> {
                    if (throwable.getMessage() != null){
                        apiResultLiveData.setValue(Resource.error(throwable.getMessage()));
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
