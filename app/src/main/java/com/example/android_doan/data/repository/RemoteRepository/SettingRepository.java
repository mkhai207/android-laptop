package com.example.android_doan.data.repository.RemoteRepository;

import com.example.android_doan.data.api.user.UserService;
import com.example.android_doan.data.model.UserModel;
import com.example.android_doan.data.model.request.ChangePasswordRequest;
import com.example.android_doan.data.model.response.CommonResponse;
import com.example.android_doan.data.model.response.UploadFileResponse;
import com.example.android_doan.data.model.response.User;
import com.example.android_doan.data.model.response.UserResponse;

import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class SettingRepository {
    public Single<UserResponse> getUser(String userId){
        return UserService.getInstance().getUser(userId);
    }

    public Single<UserResponse> updateUser(UserModel userModel){
        return UserService.getInstance().updateUser(userModel);
    }

    public Single<CommonResponse> changePassword(ChangePasswordRequest request){
        return UserService.getInstance().changePassword(request);
    }

    public Single<CommonResponse> logout(){
        return UserService.getInstance().logout();
    }

    public Single<UploadFileResponse> uploadFile(RequestBody folder, MultipartBody.Part file){
        return UserService.getInstance().uploadFile(folder, file);
    }
}
