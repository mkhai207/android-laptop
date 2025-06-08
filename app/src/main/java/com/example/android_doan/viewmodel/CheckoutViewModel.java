package com.example.android_doan.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.android_doan.data.model.AddressModel;
import com.example.android_doan.data.model.request.OrderRequest;
import com.example.android_doan.data.model.response.AddressResponse;
import com.example.android_doan.data.repository.RemoteRepository.CheckoutRepository;
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

public class CheckoutViewModel extends ViewModel {
    private CheckoutRepository checkoutRepository;
    private MutableLiveData<AddressModel> address = new MutableLiveData<>();
    private MutableLiveData<List<AddressResponse>> addressLiveData = new MutableLiveData<>();
    private MutableLiveData<Resource> apiResultLiveData = new MutableLiveData<>();
    private CompositeDisposable disposables = new CompositeDisposable();

    public CheckoutViewModel(CheckoutRepository repository) {
        this.checkoutRepository = repository;
    }

    public MutableLiveData<List<AddressResponse>> getAddressLiveData() {
        return addressLiveData;
    }

    public MutableLiveData<Resource> getApiResultLiveData() {
        return apiResultLiveData;
    }

    public MutableLiveData<AddressModel> getAddress() {
        return address;
    }

    public void setAddress(AddressModel newAddress) {
        address.setValue(newAddress);
    }

    public void placeOrder(OrderRequest request, IActionResultCallback callback) {
        Disposable disposable = checkoutRepository.placeOrder(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response != null && response.getStatusCode() == 201) {
                        callback.actionResultCallback(true);
                    } else {
                        callback.actionResultCallback(false);
                    }
                }, throwable -> {
                    callback.actionResultCallback(false);
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

    public void clearCart(IActionResultCallback callback) {
        Disposable disposable = checkoutRepository.clearCart()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response != null && response.getStatusCode() == 200) {
                        callback.actionResultCallback(true);
                    } else {
                        callback.actionResultCallback(false);
                    }
                }, throwable -> {
                    callback.actionResultCallback(false);
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

    public void getAddressDefault(String filter) {
        apiResultLiveData.setValue(Resource.loading());
        Disposable disposable = checkoutRepository.getAddressDefault(filter)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response != null && response.getStatusCode() == 200) {
                        addressLiveData.setValue(response.getData().getResult());
                        apiResultLiveData.setValue(Resource.success("getAddressDefault"));
                    } else {
                        apiResultLiveData.setValue(Resource.error("getAddressDefault"));
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

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }

    public interface IActionResultCallback {
        void actionResultCallback(boolean isSuccess);
    }
}
