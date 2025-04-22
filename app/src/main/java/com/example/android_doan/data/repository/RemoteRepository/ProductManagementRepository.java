package com.example.android_doan.data.repository.RemoteRepository;

import com.example.android_doan.data.api.user.ApiService;
import com.example.android_doan.data.model.ProductModel;
import com.example.android_doan.data.model.request.CreateProductRequest;
import com.example.android_doan.data.model.request.UpdateProductRequest;
import com.example.android_doan.data.model.response.BrandResponse;
import com.example.android_doan.data.model.response.CreateProductResponse;
import com.example.android_doan.data.model.response.GetAllCategoriesResponse;
import com.example.android_doan.data.model.response.ProductResponse;
import com.example.android_doan.data.model.response.UploadFileResponse;

import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ProductManagementRepository {
    public Single<ProductResponse> getAllProduct(int page, int size, String sort, String filter){
        return ApiService.getInstance().getAllProduct(page, size, sort, filter);
    }

    public Single<CreateProductResponse> createProduct(CreateProductRequest request){
        return ApiService.getInstance().createProduct(request);
    }

    public Single<CreateProductResponse> updateProduct(UpdateProductRequest request){
        return ApiService.getInstance().updateProduct(request);
    }

    public Single<BrandResponse> getAllBrand(){
        return ApiService.getInstance().getBrands();
    }

    public Single<GetAllCategoriesResponse> getAllCategories(){
        return ApiService.getInstance().getAllCategoriesNoPage();
    }

    public Single<UploadFileResponse> uploadFile(RequestBody folder, MultipartBody.Part file){
        return ApiService.getInstance().uploadFile(folder, file);
    }
}
