package com.example.android_doan.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.android_doan.data.api.user.UserService;
import com.example.android_doan.data.model.ProductModel;
import com.example.android_doan.data.model.response.Meta;
import com.example.android_doan.data.model.response.OrderResponse;
import com.example.android_doan.data.repository.LocalRepository.DataLocalManager;
import com.example.android_doan.data.repository.RemoteRepository.OrderRepository;
import com.example.android_doan.utils.Resource;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class OrderViewModel extends ViewModel {
    private OrderRepository repository;
    private CompositeDisposable disposables = new CompositeDisposable();
    private MutableLiveData<List<OrderResponse.OrderData>> orderLiveData = new MutableLiveData<>();
    private MutableLiveData<Resource> actionResult = new MutableLiveData<>(new Resource(Resource.Status.SUCCESS, ""));

    private int currentPage = 0;
    private int pages = 1;
    private int pageSize = 5;
    private List<OrderResponse.OrderData> mListOrder = new ArrayList<>();

    public MutableLiveData<List<OrderResponse.OrderData>> getOrderLiveData(){
        return orderLiveData;
    }

    public MutableLiveData<Resource> getActionResult(){
        return actionResult;
    }
    public OrderViewModel(OrderRepository repository) {
        this.repository = repository;
    }

//    public void getOrders(String userId){
//        actionResult.setValue(Resource.loading());
//        Disposable disposable = repository.getOrder(userId)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(response -> {
//                    if (response != null && response.getData() != null) {
//                        orderLiveData.setValue(response.getData());
//                        actionResult.setValue(Resource.success("Load orders success"));
//                    } else {
//                        actionResult.setValue(Resource.error("Load orders failure"));
//                    }
//                }, throwable -> {
//                    if (throwable.getMessage() != null) {
//                        actionResult.setValue(Resource.success(throwable.getMessage()));
//                    }
//                });
//        disposables.add(disposable);
//    }

    public void getOrders(int page){
        if (actionResult.getValue().getStatus() == Resource.Status.LOADING || page > pages){
            return;
        }
        actionResult.setValue(Resource.loading());
        String userId = DataLocalManager.getUserId();
        Disposable disposable = UserService.getInstance().getOrder(userId, page, pageSize)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(orderResponse -> {
                    if (orderResponse != null){
                        Meta meta = orderResponse.getData().getMeta();
                        if (meta != null){
                            currentPage = meta.getPage();
                            pageSize = meta.getPageSize();
                            pages = meta.getPages();
                        }
                        List<OrderResponse.OrderData> orders = orderResponse.getData().getResult();
                        if (orders != null){
                            mListOrder.addAll(orders);
                            orderLiveData.setValue(mListOrder);
                            actionResult.setValue(Resource.success("Load order success"));
                        }
                    }
                }, throwable -> {
                    if (throwable.getMessage() != null){
                        actionResult.setValue(Resource.error("Load order failure"));
                    }
                });
        disposables.add(disposable);
    }

    public void loadNextPage(){
        if (currentPage < pages){
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
