package com.example.android_doan.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.android_doan.data.model.response.OrderResponse;
import com.example.android_doan.data.repository.RemoteRepository.OrderRepository;
import com.example.android_doan.utils.Resource;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class OrderViewModel extends ViewModel {
    private OrderRepository repository;
    private CompositeDisposable disposables = new CompositeDisposable();
    private MutableLiveData<List<OrderResponse.OrderData>> orderLiveData = new MutableLiveData<>();
    private MutableLiveData<Resource> actionResult = new MutableLiveData<>();

    public MutableLiveData<List<OrderResponse.OrderData>> getOrderLiveData(){
        return orderLiveData;
    }

    public MutableLiveData<Resource> getActionResult(){
        return actionResult;
    }
    public OrderViewModel(OrderRepository repository) {
        this.repository = repository;
    }

    public void getOrders(String userId){
        actionResult.setValue(Resource.loading());
        Disposable disposable = repository.getOrder(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response != null && response.getData() != null) {
                        actionResult.setValue(Resource.success("Load orders success"));
                    } else {
                        actionResult.setValue(Resource.error("Load orders failure"));
                    }
                }, throwable -> {
                    if (throwable.getMessage() != null) {
                        actionResult.setValue(Resource.success(throwable.getMessage()));
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
