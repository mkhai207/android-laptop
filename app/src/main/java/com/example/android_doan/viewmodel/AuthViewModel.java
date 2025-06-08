package com.example.android_doan.viewmodel;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.android_doan.data.model.UserModel;
import com.example.android_doan.data.model.request.LoginRequest;
import com.example.android_doan.data.model.request.RegisterRequest;
import com.example.android_doan.data.repository.LocalRepository.DataLocalManager;
import com.example.android_doan.data.repository.RemoteRepository.AuthRepository;
import com.example.android_doan.utils.Resource;

import org.json.JSONObject;

import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

public class AuthViewModel extends ViewModel {
    private final MutableLiveData<UserModel> userLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> statusRegister = new MutableLiveData<>(false);
    private final CompositeDisposable disposables = new CompositeDisposable();
    private AuthRepository authRepository;
    private MutableLiveData<Resource> actionResult = new MutableLiveData<>();

    public AuthViewModel(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public MutableLiveData<Resource> getActionResult() {
        return actionResult;
    }

    public LiveData<UserModel> getUserLiveData() {
        return userLiveData;
    }

    public MutableLiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    public MutableLiveData<Boolean> getStatusRegister() {
        return statusRegister;
    }

    public void login(LoginRequest loginRequest) {
        Disposable disposable = authRepository.login(loginRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response != null) {
                        userLiveData.setValue(response.getData().getUser());
                        DataLocalManager.saveAccessToken(response.getData().getAccessToken());
                        DataLocalManager.saveUserId(String.valueOf(response.getData().getUser().getId()));
                        DataLocalManager.saveRole(response.getData().getUser().getRole().getCode());
                    }
                }, throwable -> {
                    if (throwable instanceof HttpException) {
                        HttpException httpException = (HttpException) throwable;
                        ResponseBody errorBody = Objects.requireNonNull(httpException.response()).errorBody();
                        if (errorBody != null) {
                            try {
                                String errorJson = errorBody.string();
                                JSONObject jsonObject = new JSONObject(errorJson);
                                String errorMessage = jsonObject.optString("message", "Failed");
                                errorLiveData.setValue(errorMessage);
                            } catch (Exception e) {
                                errorLiveData.setValue("Unknown error");
                            }
                        } else {
                            errorLiveData.setValue("Unknown server error");
                        }
                    } else {
                        errorLiveData.setValue(throwable.getMessage());
                    }
                });
        disposables.add(disposable);
    }

    public void register(RegisterRequest registerRequest) {
        Disposable disposable = authRepository.register(registerRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response != null && response.getStatusCode() != 201) {
                        statusRegister.setValue(true);
                    } else {
                        statusRegister.setValue(false);
                    }
                }, throwable -> {
                    statusRegister.setValue(false);
                    if (throwable instanceof HttpException) {
                        HttpException httpException = (HttpException) throwable;
                        ResponseBody errorBody = Objects.requireNonNull(httpException.response()).errorBody();
                        if (errorBody != null) {
                            try {
                                String errorJson = errorBody.string();
                                JSONObject jsonObject = new JSONObject(errorJson);
                                String errorMessage = jsonObject.optString("message", "Failed");
                                errorLiveData.setValue(errorMessage);
                            } catch (Exception e) {
                                errorLiveData.setValue("Unknown error");
                            }
                        } else {
                            errorLiveData.setValue("Unknown server error");
                        }
                    } else {
                        errorLiveData.setValue(throwable.getMessage());
                    }
                });
        disposables.add(disposable);
    }

    public void logout() {
        actionResult.setValue(Resource.loading());
        Disposable disposable = authRepository.logout()
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
                    if (throwable instanceof HttpException) {
                        HttpException httpException = (HttpException) throwable;
                        ResponseBody errorBody = Objects.requireNonNull(httpException.response()).errorBody();
                        if (errorBody != null) {
                            try {
                                String errorJson = errorBody.string();
                                JSONObject jsonObject = new JSONObject(errorJson);
                                String errorMessage = jsonObject.optString("message", "Failed");
                                errorLiveData.setValue(errorMessage);
                            } catch (Exception e) {
                                errorLiveData.setValue("Unknown error");
                            }
                        } else {
                            errorLiveData.setValue("Unknown server error");
                        }
                    } else {
                        errorLiveData.setValue(throwable.getMessage());
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
