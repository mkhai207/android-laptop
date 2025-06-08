package com.example.android_doan.data.repository.RemoteRepository;

import com.example.android_doan.base.BaseResponse;
import com.example.android_doan.data.api.user.ApiService;
import com.example.android_doan.data.model.FileData;
import com.example.android_doan.data.model.UserModel;
import com.example.android_doan.data.model.request.ChangePasswordRequest;

import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class SettingRepository {
    public Single<BaseResponse<UserModel>> getUser(String userId) {
        return ApiService.getInstance().getUser(userId);
    }

    public Single<BaseResponse<UserModel>> updateUser(UserModel userModel) {
        return ApiService.getInstance().updateUser(userModel);
    }

    public Single<BaseResponse<String>> changePassword(ChangePasswordRequest request) {
        return ApiService.getInstance().changePassword(request);
    }

    public Single<BaseResponse<String>> logout() {
        return ApiService.getInstance().logout();
    }

    public Single<BaseResponse<FileData>> uploadFile(RequestBody folder, MultipartBody.Part file) {
        return ApiService.getInstance().uploadFile(folder, file);
    }
}
