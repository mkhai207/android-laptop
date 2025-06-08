package com.example.android_doan.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.android_doan.data.model.request.AddToCartRequest;
import com.example.android_doan.data.model.response.GetCartResponse;
import com.example.android_doan.data.repository.RemoteRepository.CartRepository;
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

public class CartViewModel extends ViewModel {
    private CartRepository cartRepository;
    private MutableLiveData<List<GetCartResponse.Data>> itemCartLiveData = new MutableLiveData<>();

    private MutableLiveData<Double> totalPriceCart = new MutableLiveData<>();

    private MutableLiveData<Resource> apiResultLiveData = new MutableLiveData<>();

    private CompositeDisposable disposables = new CompositeDisposable();


    public CartViewModel(CartRepository repository) {
        this.cartRepository = repository;
    }

    public MutableLiveData<List<GetCartResponse.Data>> getItemCartLiveData() {
        return itemCartLiveData;
    }

    public MutableLiveData<Double> getTotalPriceCart() {
        return totalPriceCart;
    }

    public MutableLiveData<Resource> getActionResult() {
        return apiResultLiveData;
    }

    public void getCart() {
        apiResultLiveData.setValue(Resource.loading());
        Disposable disposable = cartRepository.getCart()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response.getStatusCode() == 200 && response.getData() != null) {
                        itemCartLiveData.setValue(response.getData());
                        double total = 0.0;
                        for (GetCartResponse.Data item : response.getData()) {
                            total += item.getQuantity() * item.getProduct().getPrice();
                        }
                        totalPriceCart.setValue(total);

                        apiResultLiveData.setValue(Resource.success("Get carts success"));
                    } else {
                        apiResultLiveData.setValue(Resource.error("Get carts failure"));
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

    public void updateQuantity(AddToCartRequest request) {
        apiResultLiveData.setValue(Resource.loading());
        Disposable disposable = cartRepository.updateQuantity(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response.getStatusCode() == 200) {
                        apiResultLiveData.setValue(Resource.success("Update quantity success"));
                        getCart();
                    } else {
                        apiResultLiveData.setValue(Resource.error("Update quantity failure"));
                        getCart();
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

    public void removeCart(String cartId) {
        apiResultLiveData.setValue(Resource.loading());
        Disposable disposable = cartRepository.removeCart(cartId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response.getStatusCode() == 200) {
                        apiResultLiveData.setValue(Resource.success("Remove success"));
                        getCart();
                    } else {
                        apiResultLiveData.setValue(Resource.error("Remove failure"));
                        getCart();
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
}
