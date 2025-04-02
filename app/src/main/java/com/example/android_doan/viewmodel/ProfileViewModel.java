package com.example.android_doan.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.android_doan.data.model.UserModel;
import com.example.android_doan.data.repository.LocalRepository.DataLocalManager;
import com.example.android_doan.data.repository.RemoteRepository.ProfileRepository;
import com.example.android_doan.utils.Resource;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ProfileViewModel extends ViewModel {
    private ProfileRepository repository;

    private CompositeDisposable disposables = new CompositeDisposable();
    private MutableLiveData<Resource> actionResult = new MutableLiveData<>();

    private MutableLiveData<UserModel> userInfo = new MutableLiveData<>();

    public MutableLiveData<UserModel> getUserInfo(){
        return userInfo;
    }

    public MutableLiveData<Resource> getActionResult(){
        return actionResult;
    }

    public ProfileViewModel(ProfileRepository repository) {
        this.repository = repository;
    }

    public void getAccount(String userId){
        actionResult.setValue(Resource.loading());
        Disposable disposable = repository.getAccount()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response != null && response.getUser().getUserModel() != null){
                        userInfo.setValue(response.getUser().getUserModel());
                        actionResult.setValue(Resource.success("Loading account info success"));
                    } else {
                        actionResult.setValue(Resource.error("Loading account info failure"));
                    }
                }, throwable -> {
                    if (throwable.getMessage() != null){
                        actionResult.setValue(Resource.error(throwable.getMessage()));
                    }
                });
        disposables.add(disposable);
    }

    public void logout(){
        actionResult.setValue(Resource.loading());
        Disposable disposable = repository.logout()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response != null && response.getStatusCode() == 200){
                        DataLocalManager.clearAccessToken();
                        DataLocalManager.clearRefreshToken();
                        DataLocalManager.clearUserId();
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
