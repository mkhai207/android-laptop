package com.example.android_doan.viewmodel;


import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.android_doan.data.api.login.LoginService;
import com.example.android_doan.data.model.request.LoginRequest;
import com.example.android_doan.data.model.request.RegisterRequest;
import com.example.android_doan.data.model.UserModel;
import com.example.android_doan.data.repository.LocalRepository.DataLocalManager;
import com.example.android_doan.data.repository.RemoteRepository.AuthRepository;
import com.example.android_doan.utils.Resource;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class AuthViewModel extends ViewModel {
    private Context context;
    private AuthRepository authRepository;
    private final MutableLiveData<UserModel> userLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();
    private MutableLiveData<Resource> actionResult = new MutableLiveData<>();
    private final MutableLiveData<Boolean> statusRegister = new MutableLiveData<>(false);
    private final CompositeDisposable disposables = new CompositeDisposable();

    public AuthViewModel(Context context, AuthRepository authRepository) {
        this.context = context;
        this.authRepository = authRepository;
    }

    public MutableLiveData<Resource> getActionResult(){
        return actionResult;
    }
    public LiveData<UserModel> getUserLiveData(){
        return userLiveData;
    }

    public MutableLiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    public MutableLiveData<Boolean> getStatusRegister(){
        return statusRegister;
    }

    public void login(LoginRequest loginRequest){
        Disposable disposable = authRepository.login(loginRequest)
                .subscribe(response -> {
                    if (response != null){
                        userLiveData.setValue(response.getData().getUser());
                        DataLocalManager.saveAccessToken(response.getData().getAccessToken());
                        Log.d("lkhai4617", String.valueOf(response.getData().getUser().getId()));
                        DataLocalManager.saveUserId(String.valueOf(response.getData().getUser().getId()));
                        DataLocalManager.saveRole(response.getData().getUser().getRole().getCode());
                    }
                }, throwable -> {
                    errorLiveData.setValue(throwable.getMessage());
                });
        disposables.add(disposable);
    }

    public void register(RegisterRequest registerRequest){
        Disposable disposable = LoginService.getInstance().register(registerRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response->{
                    String successCode = "201";
                    if (response != null && !response.getStatusCode().equals(successCode)){
                        Toast.makeText(context, "Success", Toast.LENGTH_LONG).show();
                        statusRegister.setValue(true);
                    } else {
                        statusRegister.setValue(false);
                    }
                }, throwable -> {
                    Toast.makeText(context, throwable.getMessage(), Toast.LENGTH_LONG).show();
                    statusRegister.setValue(false);
                });
        disposables.add(disposable);
    }

    public void logout(){
        actionResult.setValue(Resource.loading());
        Disposable disposable = authRepository.logout()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response != null && response.getStatusCode() == 200){
                        DataLocalManager.clearAccessToken();
                        DataLocalManager.clearRefreshToken();
                        DataLocalManager.clearUserId();
                        DataLocalManager.clearRole();
                        actionResult.setValue(Resource.success("Logout success"));
                    } else {
                        actionResult.setValue(Resource.error("Logout failure"));
                    }
                }, throwable -> {
                    if (throwable.getMessage() != null){
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
