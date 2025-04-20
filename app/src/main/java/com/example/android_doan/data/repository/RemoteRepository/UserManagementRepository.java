package com.example.android_doan.data.repository.RemoteRepository;

import com.example.android_doan.data.api.user.ApiService;
import com.example.android_doan.data.model.UserModel;
import com.example.android_doan.data.model.request.CreateUserRequest;
import com.example.android_doan.data.model.response.CommonResponse;
import com.example.android_doan.data.model.response.CreateUserResponse;
import com.example.android_doan.data.model.response.GetAllUserResponse;
import com.example.android_doan.data.model.response.UploadFileResponse;
import com.example.android_doan.data.model.response.UserResponse;

import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class UserManagementRepository {
    public Single<GetAllUserResponse> getAllUser(int page, int size, String sort){
        return ApiService.getInstance().getAllUser(page, size, sort);
    }

    public Single<CreateUserResponse> createUser(CreateUserRequest request){
        return ApiService.getInstance().createUser(request);
    }

    public Single<UploadFileResponse> uploadFile(RequestBody folder, MultipartBody.Part file){
        return ApiService.getInstance().uploadFile(folder, file);
    }

    public Single<CommonResponse> deleteUser(int id){
        return ApiService.getInstance().deleteUser(id);
    }

    public Single<UserResponse> updateUser(UserModel userModel){
        return ApiService.getInstance().updateUser(userModel);
    }
}
