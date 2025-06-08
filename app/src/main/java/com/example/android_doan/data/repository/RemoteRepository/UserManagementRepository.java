package com.example.android_doan.data.repository.RemoteRepository;

import com.example.android_doan.base.BasePagingResponse;
import com.example.android_doan.base.BaseResponse;
import com.example.android_doan.data.api.user.ApiService;
import com.example.android_doan.data.model.FileData;
import com.example.android_doan.data.model.UserModel;
import com.example.android_doan.data.model.request.CreateUserRequest;

import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class UserManagementRepository {
    public Single<BaseResponse<BasePagingResponse<UserModel>>> getAllUser(int page, int size, String sort) {
        return ApiService.getInstance().getAllUser(page, size, sort);
    }

    public Single<BaseResponse<UserModel>> createUser(CreateUserRequest request) {
        return ApiService.getInstance().createUser(request);
    }

    public Single<BaseResponse<FileData>> uploadFile(RequestBody folder, MultipartBody.Part file) {
        return ApiService.getInstance().uploadFile(folder, file);
    }

    public Single<BaseResponse<String>> deleteUser(int id) {
        return ApiService.getInstance().deleteUser(id);
    }

    public Single<BaseResponse<UserModel>> updateUser(UserModel userModel) {
        return ApiService.getInstance().updateUser(userModel);
    }
}
