package com.example.android_doan.data.repository.RemoteRepository;

import com.example.android_doan.data.api.user.ApiService;
import com.example.android_doan.data.model.UserModel;
import com.example.android_doan.data.model.request.ChangePasswordRequest;
import com.example.android_doan.data.model.response.CommonResponse;
import com.example.android_doan.data.model.response.UploadFileResponse;
import com.example.android_doan.data.model.response.UserResponse;

import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class SettingRepository {
    public Single<UserResponse> getUser(String userId){
        return ApiService.getInstance().getUser(userId);
    }

    public Single<UserResponse> updateUser(UserModel userModel){
        return ApiService.getInstance().updateUser(userModel);
    }

    public Single<CommonResponse> changePassword(ChangePasswordRequest request){
        return ApiService.getInstance().changePassword(request);
    }

    public Single<CommonResponse> logout(){
        return ApiService.getInstance().logout();
    }

    public Single<UploadFileResponse> uploadFile(RequestBody folder, MultipartBody.Part file){
        return ApiService.getInstance().uploadFile(folder, file);
    }
}
