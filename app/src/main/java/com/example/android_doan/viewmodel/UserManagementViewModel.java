package com.example.android_doan.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.android_doan.data.model.UserModel;
import com.example.android_doan.data.model.request.CreateUserRequest;
import com.example.android_doan.data.model.response.CommonResponse;
import com.example.android_doan.data.model.response.CreateUserResponse;
import com.example.android_doan.data.model.response.Meta;
import com.example.android_doan.data.model.response.UploadFileResponse;
import com.example.android_doan.data.repository.RemoteRepository.UserManagementRepository;
import com.example.android_doan.utils.Resource;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class UserManagementViewModel extends ViewModel {
    private UserManagementRepository userManagementRepository;

    private MutableLiveData<List<UserModel>> usersLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoadingLiveData = new MutableLiveData<>(false);
    private MutableLiveData<UserModel> userLiveData = new MutableLiveData<>();
    private MutableLiveData<Resource> apiResultLiveData = new MutableLiveData<>();
    private MutableLiveData<UploadFileResponse> fileLiveData = new MutableLiveData<>();
    private CompositeDisposable disposables = new CompositeDisposable();
    private List<UserModel> mListUser = new ArrayList<>();
    private int currentPage = 0;
    private int pages = 1;
    private int pageSize = 10;

    public MutableLiveData<Resource> getApiResultLiveData(){
        return apiResultLiveData;
    }

    public MutableLiveData<UploadFileResponse> getFileLiveData(){
        return fileLiveData;
    }
    public MutableLiveData<UserModel> getUserLiveData(){
        return userLiveData;
    }
    public MutableLiveData<List<UserModel>> getUsersLiveData(){
        return usersLiveData;
    }

    public MutableLiveData<Boolean> getIsLoadingLiveData(){
        return isLoadingLiveData;
    }

    public UserManagementViewModel(UserManagementRepository userManagementRepository) {
        this.userManagementRepository = userManagementRepository;
    }

    public void getAllUser(int page){
        if (isLoadingLiveData.getValue() == Boolean.TRUE || page > pages){
            return;
        }
        isLoadingLiveData.setValue(true);
        String sort = "";
        Disposable disposable = userManagementRepository.getAllUser(page, pageSize, sort)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response != null && response.getStatusCode() == 200){
                        Meta meta = response.getData().getMeta();
                        if (meta != null){
                            currentPage = meta.getPage();
                            pageSize = meta.getPageSize();
                            pages = meta.getPages();
                        }
                        List<UserModel> users = response.getData().getResult();
                        if (users != null){
                            mListUser.addAll(users);
                            usersLiveData.setValue(mListUser);
                            isLoadingLiveData.setValue(false);
                        }
                        isLoadingLiveData.setValue(false);
                    }
                }, throwable -> {
                    isLoadingLiveData.setValue(false);
                    if (throwable.getMessage() != null){
                        Log.d("lkhai4617", throwable.getMessage());
                    }
                });
        disposables.add(disposable);
    }

    public void loadNextPage(){
        if (currentPage < pages){
            Log.d("lkhai4617", "load next page");
            getAllUser(currentPage + 1);
        }
    }

    public void createUser(CreateUserRequest request){
        apiResultLiveData.setValue(Resource.loading());
        Disposable disposable = userManagementRepository.createUser(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response != null && response.getStatusCode() == 201){
                        userLiveData.setValue(response.getData());
                        apiResultLiveData.setValue(Resource.success("createUser"));
                    }
                }, throwable -> {
                    if (throwable.getMessage() != null){
                        Log.d("lkhai4617", throwable.getMessage());
                    }
                });
        disposables.add(disposable);
    }

    public void uploadFile(RequestBody folder, MultipartBody.Part file){
        apiResultLiveData.setValue(Resource.loading());
        Disposable disposable = userManagementRepository.uploadFile(folder, file)
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

    public void deleteUser(int id){
        apiResultLiveData.setValue(Resource.loading());
        Disposable disposable = userManagementRepository.deleteUser(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response != null && response.getStatusCode() == 200){
                        loadNextPage();
                        apiResultLiveData.setValue(Resource.success("deleteUser"));
                    } else {
                        apiResultLiveData.setValue(Resource.error("deleteUser"));
                    }
                }, throwable -> {
                    if (throwable.getMessage() != null){
                        apiResultLiveData.setValue(Resource.error(throwable.getMessage()));
                    }
                });
        disposables.add(disposable);
    }

    public void updateUser(UserModel userModel){
        apiResultLiveData.setValue(Resource.loading());
        Disposable disposable = userManagementRepository.updateUser(userModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response ->{
                    if (response != null && response.getStatusCode() ==200){
                        apiResultLiveData.setValue(Resource.success("updateUser"));
                    } else {
                        apiResultLiveData.setValue(Resource.error("updateUser"));
                    }
                }, throwable -> {
                    if (throwable.getMessage()!=null){
                        apiResultLiveData.setValue(Resource.error(throwable.getMessage()));
                    }
                });
        disposables.add(disposable);
    }

    public void refreshUsers() {
        currentPage = 0;
        mListUser.clear();
        usersLiveData.setValue(mListUser);
        loadNextPage();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }
}
