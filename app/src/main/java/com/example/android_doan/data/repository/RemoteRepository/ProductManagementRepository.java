package com.example.android_doan.data.repository.RemoteRepository;

import com.example.android_doan.base.BasePagingResponse;
import com.example.android_doan.base.BaseResponse;
import com.example.android_doan.data.api.user.ApiService;
import com.example.android_doan.data.model.BrandModel;
import com.example.android_doan.data.model.CategoryModel;
import com.example.android_doan.data.model.FileData;
import com.example.android_doan.data.model.ProductModel;
import com.example.android_doan.data.model.request.CreateProductRequest;
import com.example.android_doan.data.model.request.UpdateProductRequest;

import java.util.List;

import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ProductManagementRepository {
    public Single<BaseResponse<BasePagingResponse<ProductModel>>> getAllProduct(int page, int size, String sort, String filter) {
        return ApiService.getInstance().getAllProduct(page, size, sort, filter);
    }

    public Single<BaseResponse<ProductModel>> createProduct(CreateProductRequest request) {
        return ApiService.getInstance().createProduct(request);
    }

    public Single<BaseResponse<ProductModel>> updateProduct(UpdateProductRequest request) {
        return ApiService.getInstance().updateProduct(request);
    }

    public Single<BaseResponse<BasePagingResponse<BrandModel>>> getAllBrand() {
        return ApiService.getInstance().getBrands(0, Integer.MAX_VALUE, "createdAt,desc");
    }

    public Single<BaseResponse<BasePagingResponse<CategoryModel>>> getAllCategories() {
        return ApiService.getInstance().getAllCategoriesNoPage();
    }

    public Single<BaseResponse<FileData>> uploadFile(RequestBody folder, MultipartBody.Part file) {
        return ApiService.getInstance().uploadFile(folder, file);
    }

    public Single<BaseResponse<List<FileData>>> uploadMultipleFile(RequestBody folder, List<MultipartBody.Part> files) {
        return ApiService.getInstance().uploadMultipleFile(folder, files);
    }
}
