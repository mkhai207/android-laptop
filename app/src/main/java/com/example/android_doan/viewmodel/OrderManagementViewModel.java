package com.example.android_doan.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.android_doan.data.model.BrandModel;
import com.example.android_doan.data.model.request.UpdateOrderRequest;
import com.example.android_doan.data.model.response.OrderAdminResponse;
import com.example.android_doan.data.repository.RemoteRepository.OrderManagementRepository;
import com.example.android_doan.utils.Resource;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class OrderManagementViewModel extends ViewModel {
    private OrderManagementRepository orderManagementRepository;
    private MutableLiveData<List<OrderAdminResponse>> ordersLiveData = new MutableLiveData<>();
    private MutableLiveData<Resource> apiResultLiveData = new MutableLiveData<>();
    private CompositeDisposable disposables = new CompositeDisposable();

    public MutableLiveData<List<OrderAdminResponse>> getOrdersLiveData(){
        return ordersLiveData;
    }

    public MutableLiveData<Resource> getApiResultLiveData(){
        return apiResultLiveData;
    }
    public OrderManagementViewModel(OrderManagementRepository orderManagementRepository) {
        this.orderManagementRepository = orderManagementRepository;
    }

    public void getAllOrder(String filter){
        apiResultLiveData.setValue(Resource.loading());
        Disposable disposable = orderManagementRepository.getAllOrder(filter)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response ->{
                    if (response != null && response.getStatusCode() == 200){
                        ordersLiveData.setValue(response.getData().getResult());
                        apiResultLiveData.setValue(Resource.success("getAllOrder"));
                    } else {
                        apiResultLiveData.setValue(Resource.error("getAllOrder"));
                    }
                }, throwable -> {
                    if (throwable.getMessage()!=null){
                        apiResultLiveData.setValue(Resource.error(throwable.getMessage()));
                    }
                });
        disposables.add(disposable);
    }

    public void updateOrder(UpdateOrderRequest request){
        apiResultLiveData.setValue(Resource.loading());
        Disposable disposable = orderManagementRepository.updateOrder(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response ->{
                    if (response != null && response.getStatusCode() == 200){
                        apiResultLiveData.setValue(Resource.success("updateOrder"));
                    } else {
                        apiResultLiveData.setValue(Resource.error("updateOrder"));
                    }
                }, throwable -> {
                    if (throwable.getMessage()!=null){
                        apiResultLiveData.setValue(Resource.error(throwable.getMessage()));
                    }
                });
        disposables.add(disposable);
    }

    public void refresh(String filter){
        getAllOrder(filter);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }
}
