package com.example.android_doan.data.repository.RemoteRepository;

import com.example.android_doan.base.BasePagingResponse;
import com.example.android_doan.base.BaseResponse;
import com.example.android_doan.data.api.user.ApiService;
import com.example.android_doan.data.model.CategoryModel;
import com.example.android_doan.data.model.request.CreateCategoryRequest;
import com.example.android_doan.data.model.request.UpdateCategoryRequest;

import io.reactivex.Single;

public class CategoryManagementRepository {
    public Single<BaseResponse<BasePagingResponse<CategoryModel>>> getAllCategories(int page, int size, String sort) {
        return ApiService.getInstance().getAllCategories(page, size, sort);
    }

    public Single<BaseResponse<CategoryModel>> createCategory(CreateCategoryRequest request) {
        return ApiService.getInstance().createCategory(request);
    }

    public Single<BaseResponse<String>> deleteCategory(int categoryId) {
        return ApiService.getInstance().deleteCategory(categoryId);
    }

    public Single<BaseResponse<CategoryModel>> updateCategory(UpdateCategoryRequest request) {
        return ApiService.getInstance().updateCategory(request);
    }
}
