package com.example.android_doan.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.android_doan.data.model.response.UserResponse;
import com.example.android_doan.data.repository.RemoteRepository.SettingRepository;
import com.example.android_doan.utils.Resource;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SettingViewModel extends ViewModel {
    private SettingRepository settingRepository;

    private MutableLiveData<UserResponse> userInfo = new MutableLiveData<>();
    private MutableLiveData<Resource> actionResult = new MutableLiveData<>();

    private CompositeDisposable disposables = new CompositeDisposable();

    public MutableLiveData<UserResponse> getUserInfo(){
        return userInfo;
    }

    public MutableLiveData<Resource> getActionResult(){
        return actionResult;
    }

    public SettingViewModel(SettingRepository settingRepository) {
        this.settingRepository = settingRepository;
    }

    public void getUser(String userId){
        actionResult.setValue(Resource.loading());
        Disposable disposable = settingRepository.getUser(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response!= null && response.getStatusCode() == 200){
                        userInfo.setValue(response);
                        actionResult.setValue(Resource.success("Loading success"));
                    } else {
                        actionResult.setValue(Resource.error("Loading failure"));
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
