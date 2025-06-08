package com.example.android_doan.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.android_doan.data.model.request.CreateAddressRequest;
import com.example.android_doan.data.model.response.AddressResponse;
import com.example.android_doan.data.repository.RemoteRepository.AddressRepository;
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

public class AddressViewModel extends ViewModel {
    private AddressRepository addressRepository;
    private MutableLiveData<List<AddressResponse>> addressesLiveData = new MutableLiveData<>();
    private MutableLiveData<Resource> apiResultLiveData = new MutableLiveData<>();
    private CompositeDisposable disposables = new CompositeDisposable();

    public AddressViewModel(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public MutableLiveData<List<AddressResponse>> getAddressesLiveData() {
        return addressesLiveData;
    }

    public MutableLiveData<Resource> getApiResultLiveData() {
        return apiResultLiveData;
    }

    public void getAddresses(String filter) {
        apiResultLiveData.setValue(Resource.loading());
        Disposable disposable = addressRepository.getAddresses(filter)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response != null && response.getStatusCode() == 200) {
                        addressesLiveData.setValue(response.getData().getResult());
                        apiResultLiveData.setValue(Resource.success("getAddresses"));
                    } else {
                        apiResultLiveData.setValue(Resource.error("getAddresses"));
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
                                    String errorMessage = jsonObject.optString("message", "Sign up failed");
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


    public void createAddress(CreateAddressRequest request) {
        apiResultLiveData.setValue(Resource.loading());
        Disposable disposable = addressRepository.createAddress(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response != null && response.getStatusCode() == 201) {
//                        brandsLiveData.setValue(response.getData());
                        apiResultLiveData.setValue(Resource.success("createAddress"));
                    } else {
                        apiResultLiveData.setValue(Resource.error("createAddress"));
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
                                    String errorMessage = jsonObject.optString("message", "Sign up failed");
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

    public void updateAddress(AddressResponse address) {
        apiResultLiveData.setValue(Resource.loading());
        Disposable disposable = addressRepository.updateAddress(address)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response != null && response.getStatusCode() == 200) {
                        apiResultLiveData.setValue(Resource.success("updateAddress"));
                    } else {
                        apiResultLiveData.setValue(Resource.error("updateAddress"));
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
                                    String errorMessage = jsonObject.optString("message", "Sign up failed");
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

    public void deleteAddress(int addressId) {
        apiResultLiveData.setValue(Resource.loading());
        Disposable disposable = addressRepository.deleteAddress(addressId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response != null && response.getStatusCode() == 200) {
                        apiResultLiveData.setValue(Resource.success("deleteAddress"));
                    } else {
                        apiResultLiveData.setValue(Resource.error("deleteAddress"));
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
                                    String errorMessage = jsonObject.optString("message", "Sign up failed");
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

    public void refresh(String filter) {
        getAddresses(filter);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }
}
