package com.example.android_doan.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.android_doan.data.model.UserModel;
import com.example.android_doan.data.repository.RemoteRepository.AdminHomeRepository;
import com.example.android_doan.utils.Resource;

import org.json.JSONObject;

import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

public class AdminHomeViewModel extends ViewModel {
    private final MutableLiveData<UserModel> userLiveData = new MutableLiveData<>();
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final MutableLiveData<Resource> apiResultLiveData = new MutableLiveData<>();
    private AdminHomeRepository adminHomeRepository;

    public AdminHomeViewModel(AdminHomeRepository adminHomeRepository) {
        this.adminHomeRepository = adminHomeRepository;
    }

    public MutableLiveData<UserModel> getUserLiveData() {
        return userLiveData;
    }

    public void getUser() {
        Disposable disposable = adminHomeRepository.getAccount()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retry(throwable -> {
                    if (throwable instanceof HttpException && ((HttpException) throwable).code() == 401) {
                        Thread.sleep(500);
                        return true;
                    }
                    return false;
                })
                .subscribe(response -> {
                    if (response != null && response.getData() != null && response.getData().getUserModel() != null) {
                        userLiveData.setValue(response.getData().getUserModel());
                    }
                }, throwable -> {
                    if (throwable.getMessage() != null) {
                        if (throwable instanceof HttpException) {
                            HttpException httpException = (HttpException) throwable;
                            ResponseBody errorBody = Objects.requireNonNull(httpException.response()).errorBody();
                            if (errorBody != null) {
                                try {
                                    String errorJson = errorBody.string();
                                    JSONObject jsonObject = new JSONObject(errorJson);
                                    String errorMessage = jsonObject.optString("message", "Call api failed");
                                    apiResultLiveData.setValue(Resource.error(errorMessage));
                                } catch (Exception e) {
                                    apiResultLiveData.setValue(Resource.error("Unknown error"));
                                }
                            } else {
                                apiResultLiveData.setValue(Resource.error("Unknown server error"));
                            }
                        } else {
                            apiResultLiveData.setValue(Resource.error(throwable.getMessage()));
                        }
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
