package com.example.android_doan.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.android_doan.data.model.FileData;
import com.example.android_doan.data.model.UserModel;
import com.example.android_doan.data.model.request.CreateUserRequest;
import com.example.android_doan.data.model.response.Meta;
import com.example.android_doan.data.repository.RemoteRepository.UserManagementRepository;
import com.example.android_doan.utils.Resource;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

public class UserManagementViewModel extends ViewModel {
    private UserManagementRepository userManagementRepository;

    private MutableLiveData<List<UserModel>> usersLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoadingLiveData = new MutableLiveData<>(false);
    private MutableLiveData<UserModel> userLiveData = new MutableLiveData<>();
    private MutableLiveData<Resource> apiResultLiveData = new MutableLiveData<>(new Resource(Resource.Status.SUCCESS, ""));
    private MutableLiveData<FileData> fileLiveData = new MutableLiveData<>();
    private CompositeDisposable disposables = new CompositeDisposable();
    private List<UserModel> mListUser = new ArrayList<>();
    private int currentPage = 0;
    private int pages = 1;
    private int pageSize = 10;

    public UserManagementViewModel(UserManagementRepository userManagementRepository) {
        this.userManagementRepository = userManagementRepository;
    }

    public MutableLiveData<Resource> getApiResultLiveData() {
        return apiResultLiveData;
    }

    public MutableLiveData<FileData> getFileLiveData() {
        return fileLiveData;
    }

    public MutableLiveData<UserModel> getUserLiveData() {
        return userLiveData;
    }

    public MutableLiveData<List<UserModel>> getUsersLiveData() {
        return usersLiveData;
    }

    public MutableLiveData<Boolean> getIsLoadingLiveData() {
        return isLoadingLiveData;
    }

    public void getAllUser(int page) {
        if (apiResultLiveData.getValue().getStatus() == Resource.Status.LOADING || page > pages) {
            return;
        }
        isLoadingLiveData.setValue(true);
        String sort = "createdAt,desc";
        Disposable disposable = userManagementRepository.getAllUser(page, pageSize, sort)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response != null) {
                        Meta meta = response.getData().getMeta();
                        if (meta != null) {
                            currentPage = meta.getPage();
                            pageSize = meta.getPageSize();
                            pages = meta.getPages();
                        }
                        List<UserModel> users = response.getData().getResult();
                        if (users != null) {
                            mListUser.addAll(users);
                            usersLiveData.setValue(mListUser);
                            apiResultLiveData.setValue(Resource.success("getAllUser"));
                        }
                    }
                }, throwable -> {
                    isLoadingLiveData.setValue(false);
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
                });
        disposables.add(disposable);
    }

    public void loadNextPage() {
        if (currentPage < pages) {
            Log.d("lkhai4617", "loadNextPage: currentPage: " + currentPage);
            getAllUser(currentPage + 1);
        }
    }

    public void createUser(CreateUserRequest request) {
        apiResultLiveData.setValue(Resource.loading());
        Disposable disposable = userManagementRepository.createUser(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response != null && response.getStatusCode() == 201) {
                        userLiveData.setValue(response.getData());
                        apiResultLiveData.setValue(Resource.success("createUser"));
                    }
                }, throwable -> {
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
                });
        disposables.add(disposable);
    }

    public void uploadFile(RequestBody folder, MultipartBody.Part file) {
        apiResultLiveData.setValue(Resource.loading());
        Disposable disposable = userManagementRepository.uploadFile(folder, file)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response != null && response.getStatusCode() == 200) {
                        fileLiveData.setValue(response.getData());
                        apiResultLiveData.setValue(Resource.success("uploadFile"));
                    } else {
                        apiResultLiveData.setValue(Resource.error("uploadFile"));
                    }
                }, throwable -> {
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
                });
        disposables.add(disposable);
    }

    public void deleteUser(int id) {
        apiResultLiveData.setValue(Resource.loading());
        Disposable disposable = userManagementRepository.deleteUser(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response != null && response.getStatusCode() == 200) {
                        loadNextPage();
                        apiResultLiveData.setValue(Resource.success("deleteUser"));
                    } else {
                        apiResultLiveData.setValue(Resource.error("deleteUser"));
                    }
                }, throwable -> {
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
                });
        disposables.add(disposable);
    }

    public void updateUser(UserModel userModel) {
        apiResultLiveData.setValue(Resource.loading());
        Disposable disposable = userManagementRepository.updateUser(userModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response != null && response.getStatusCode() == 200) {
                        apiResultLiveData.setValue(Resource.success("updateUser"));
                    } else {
                        apiResultLiveData.setValue(Resource.error("updateUser"));
                    }
                }, throwable -> {
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
                });
        disposables.add(disposable);
    }

    public void refresh() {
        currentPage = 0;
        pages = 1;
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
