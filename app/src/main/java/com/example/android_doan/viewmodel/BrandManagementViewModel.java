package com.example.android_doan.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.android_doan.data.model.BrandModel;
import com.example.android_doan.data.model.request.CreateBrandRequest;
import com.example.android_doan.data.repository.RemoteRepository.BrandManagementRepository;
import com.example.android_doan.utils.Resource;

import org.json.JSONObject;

import java.util.List;
import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

public class BrandManagementViewModel extends ViewModel {
    private BrandManagementRepository brandManagementRepository;
    private MutableLiveData<List<BrandModel>> brandsLiveData = new MutableLiveData<>();
    private MutableLiveData<Resource> apiResultLiveData = new MutableLiveData<>(new Resource(Resource.Status.SUCCESS, ""));
    private CompositeDisposable disposables = new CompositeDisposable();

    public BrandManagementViewModel(BrandManagementRepository brandManagementRepository) {
        this.brandManagementRepository = brandManagementRepository;
    }

    public MutableLiveData<List<BrandModel>> getBrandsLiveData() {
        return brandsLiveData;
    }

    public MutableLiveData<Resource> getApiResultLiveData() {
        return apiResultLiveData;
    }


    public void getBrand() {
        apiResultLiveData.setValue(Resource.loading());
        Disposable disposable = brandManagementRepository.getBrand()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response != null && response.getStatusCode() == 200) {
                        brandsLiveData.setValue(response.getData().getResult());
                        apiResultLiveData.setValue(Resource.success("getBrand"));
                    } else {
                        apiResultLiveData.setValue(Resource.error("getBrand"));
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

    public void createBrand(CreateBrandRequest request) {
        apiResultLiveData.setValue(Resource.loading());
        Disposable disposable = brandManagementRepository.createBrand(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response != null && response.getStatusCode() == 201) {
//                        brandsLiveData.setValue(response.getData());
                        apiResultLiveData.setValue(Resource.success("createBrand"));
                    } else {
                        apiResultLiveData.setValue(Resource.error("createBrand"));
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

    public void deleteBrand(int brandId) {
        apiResultLiveData.setValue(Resource.loading());
        Disposable disposable = brandManagementRepository.deleteBrand(brandId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response != null && response.getStatusCode() == 200) {
                        apiResultLiveData.setValue(Resource.success("deleteBrand"));
                    } else {
                        apiResultLiveData.setValue(Resource.error("deleteBrand"));
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

    public void updateBrand(BrandModel brandModel) {
        apiResultLiveData.setValue(Resource.loading());
        Disposable disposable = brandManagementRepository.updateBrand(brandModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response != null && response.getStatusCode() == 200) {
                        apiResultLiveData.setValue(Resource.success("updateBrand"));
                    } else {
                        apiResultLiveData.setValue(Resource.error("updateBrand"));
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

    public void refresh() {
        getBrand();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
