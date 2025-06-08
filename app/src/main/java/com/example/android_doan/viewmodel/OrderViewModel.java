package com.example.android_doan.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.android_doan.data.model.OrderData;
import com.example.android_doan.data.model.response.Meta;
import com.example.android_doan.data.repository.LocalRepository.DataLocalManager;
import com.example.android_doan.data.repository.RemoteRepository.OrderRepository;
import com.example.android_doan.utils.Resource;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

public class OrderViewModel extends ViewModel {
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final MutableLiveData<List<OrderData>> orderLiveData = new MutableLiveData<>();
    private final MutableLiveData<Resource> apiResultLiveData = new MutableLiveData<>(new Resource(Resource.Status.SUCCESS, ""));
    private final OrderRepository orderRepository;
    private int currentPage = 0;
    private int pages = 1;
    private int pageSize = 5;
    private List<OrderData> mListOrder = new ArrayList<>();

    public OrderViewModel(OrderRepository repository) {
        this.orderRepository = repository;
    }

    public MutableLiveData<List<OrderData>> getOrderLiveData() {
        return orderLiveData;
    }

    public MutableLiveData<Resource> getActionResult() {
        return apiResultLiveData;
    }

    public void getOrders(int page) {
        if (apiResultLiveData.getValue().getStatus() == Resource.Status.LOADING || page > pages) {
            return;
        }
        apiResultLiveData.setValue(Resource.loading());
        String userId = DataLocalManager.getUserId();
        Disposable disposable = orderRepository.getOrder(userId, page, pageSize)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(orderResponse -> {
                    if (orderResponse != null) {
                        Meta meta = orderResponse.getData().getMeta();
                        if (meta != null) {
                            currentPage = meta.getPage();
                            pageSize = meta.getPageSize();
                            pages = meta.getPages();
                        }
                        List<OrderData> orders = orderResponse.getData().getResult();
                        if (orders != null) {
                            mListOrder.addAll(orders);
                            orderLiveData.setValue(mListOrder);
                            apiResultLiveData.setValue(Resource.success("Load order success"));
                        }
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

    public void loadNextPage() {
        if (currentPage < pages) {
            Log.d("lkhai4617", "load next page");
            getOrders(currentPage + 1);
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }
}
