package com.example.android_doan.data.repository.RemoteRepository;

import com.example.android_doan.data.api.user.ApiService;
import com.example.android_doan.data.model.request.CreateCategoryRequest;
import com.example.android_doan.data.model.request.UpdateCategoryRequest;
import com.example.android_doan.data.model.response.CommonResponse;
import com.example.android_doan.data.model.response.CreateCategoryResponse;
import com.example.android_doan.data.model.response.GetAllCategoriesResponse;

import io.reactivex.Single;
import retrofit2.http.Body;

public class CategoryManagementRepository {
    public Single<GetAllCategoriesResponse> getAllCategories(int page, int size, String sort){
        return ApiService.getInstance().getAllCategories(page, size, sort);
    }

    public Single<CreateCategoryResponse> createCategory(CreateCategoryRequest request){
        return ApiService.getInstance().createCategory(request);
    }

    public Single<CommonResponse> deleteCategory(int categoryId){
        return ApiService.getInstance().deleteCategory(categoryId);
    }

    public Single<CreateCategoryResponse> updateCategory(UpdateCategoryRequest request){
        return ApiService.getInstance().updateCategory(request);
    }
}
