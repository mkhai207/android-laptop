package com.example.android_doan.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.android_doan.data.model.FileData;
import com.example.android_doan.data.model.UserModel;
import com.example.android_doan.data.model.request.ChangePasswordRequest;
import com.example.android_doan.data.repository.LocalRepository.DataLocalManager;
import com.example.android_doan.data.repository.RemoteRepository.SettingRepository;
import com.example.android_doan.utils.Resource;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class SettingViewModel extends ViewModel {
    private SettingRepository settingRepository;

    private MutableLiveData<UserModel> userInfo = new MutableLiveData<>();
    private MutableLiveData<Resource> actionResult = new MutableLiveData<>();
    private MutableLiveData<FileData> fileData = new MutableLiveData<>();

    private CompositeDisposable disposables = new CompositeDisposable();

    public SettingViewModel(SettingRepository settingRepository) {
        this.settingRepository = settingRepository;
    }

    public MutableLiveData<UserModel> getUserInfo() {
        return userInfo;
    }

    public MutableLiveData<Resource> getActionResult() {
        return actionResult;
    }

    public MutableLiveData<FileData> getFileData() {
        return fileData;
    }

    public void getUser(String userId) {
        actionResult.setValue(Resource.loading());
        Disposable disposable = settingRepository.getUser(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response != null && response.getStatusCode() == 200) {
                        userInfo.setValue(response.getData());
                        actionResult.setValue(Resource.success("getUser"));
                    } else {
                        actionResult.setValue(Resource.error("getUser"));
                    }
                }, throwable -> {
                    if (throwable.getMessage() != null) {
                        actionResult.setValue(Resource.error(throwable.getMessage()));
                    }
                });
        disposables.add(disposable);
    }

    public void updateUser(UserModel userModel) {
        actionResult.setValue(Resource.loading());
        Disposable disposable = settingRepository.updateUser(userModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response != null && response.getStatusCode() == 200) {
                        actionResult.setValue(Resource.success("updateUser"));
                    } else {
                        actionResult.setValue(Resource.error("updateUser"));
                    }
                }, throwable -> {
                    if (throwable.getMessage() != null) {
                        actionResult.setValue(Resource.error(throwable.getMessage()));
                    }
                });
        disposables.add(disposable);
    }

    public void changePassword(ChangePasswordRequest request) {
        actionResult.setValue(Resource.loading());
        Disposable disposable = settingRepository.changePassword(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response != null && response.getStatusCode() == 200) {
                        logout();
                        actionResult.setValue(Resource.success("changePassword"));
                    } else {
                        actionResult.setValue(Resource.error("changePassword"));
                    }
                }, throwable -> {
                    if (throwable.getMessage() != null) {
                        actionResult.setValue(Resource.error(throwable.getMessage()));
                    }
                });
        disposables.add(disposable);
    }

    public void logout() {
        actionResult.setValue(Resource.loading());
        Disposable disposable = settingRepository.logout()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response != null && response.getStatusCode() == 200) {
                        DataLocalManager.clearAccessToken();
                        DataLocalManager.clearRefreshToken();
                        DataLocalManager.clearUserId();
                        DataLocalManager.clearRole();
                        actionResult.setValue(Resource.success("logout"));
                    } else {
                        actionResult.setValue(Resource.error("logout"));
                    }
                }, throwable -> {
                    if (throwable.getMessage() != null) {
                        actionResult.setValue(Resource.error(throwable.getMessage()));
                    }
                });
        disposables.add(disposable);
    }

    public void uploadFile(RequestBody folder, MultipartBody.Part file) {
        actionResult.setValue(Resource.loading());
        Disposable disposable = settingRepository.uploadFile(folder, file)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response != null && response.getStatusCode() == 200) {
                        fileData.setValue(response.getData());
                        actionResult.setValue(Resource.success("uploadFile"));
                    } else {
                        actionResult.setValue(Resource.error("uploadFile"));
                    }
                }, throwable -> {
                    if (throwable.getMessage() != null) {
                        actionResult.setValue(Resource.error(throwable.getMessage()));
                    }
                });
        disposables.add(disposable);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }
}
