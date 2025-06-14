package com.example.android_doan.view.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android_doan.adapter.OrderAdminAdapter;
import com.example.android_doan.data.enums.OrderStatusEnum;
import com.example.android_doan.data.model.request.UpdateOrderRequest;
import com.example.android_doan.data.model.response.OrderAdminResponse;
import com.example.android_doan.data.repository.RemoteRepository.OrderManagementRepository;
import com.example.android_doan.databinding.FragmentOrderStatusBinding;
import com.example.android_doan.utils.CustomToast;
import com.example.android_doan.viewmodel.OrderManagementVIewModelFactory;
import com.example.android_doan.viewmodel.OrderManagementViewModel;

import java.util.ArrayList;

public class OrderStatusFragment extends Fragment {
    private FragmentOrderStatusBinding binding;
    private OrderManagementViewModel orderManagementViewModel;
    private RecyclerView rcvOrder;
    private OrderAdminAdapter orderAdminAdapter;
    private String mStatus;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null){
            mStatus = (String) args.get("status");
        }

        OrderManagementRepository orderManagementRepository = new OrderManagementRepository();
        orderManagementViewModel =
                new ViewModelProvider(
                        this,
                        new OrderManagementVIewModelFactory(orderManagementRepository)
                ).get(OrderManagementViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentOrderStatusBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupRecyclerView();
        observer();
        handleStatus();
        setupListener();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    private void setupRecyclerView() {
        orderAdminAdapter = new OrderAdminAdapter(new ArrayList<>());
        rcvOrder = binding.rvOrders;
        rcvOrder.setAdapter(orderAdminAdapter);

        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        rcvOrder.setLayoutManager(linearLayoutManager);
    }

    private void observer(){
        orderManagementViewModel.getAllOrder("status:'" + mStatus + "'");
        orderManagementViewModel.getOrdersLiveData()
                .observe(getViewLifecycleOwner(), orderAdminResponses -> {
            orderAdminAdapter.updateData(orderAdminResponses);
        });
    }

    private void handleStatus(){
        orderManagementViewModel.getApiResultLiveData().observe(getViewLifecycleOwner(), apiResult -> {
            if (apiResult != null){
                switch (apiResult.getStatus()){
                    case LOADING:
                        binding.progressBar.setVisibility(View.VISIBLE);
                        break;
                    case SUCCESS:
                        binding.progressBar.setVisibility(View.GONE);
                        switch (apiResult.getMessage()){
                            case "updateOrder":
                                orderManagementViewModel.refresh("status:'" + mStatus + "'");
                                break;
                        }
                        break;
                    case ERROR:
                        binding.progressBar.setVisibility(View.GONE);
                        Log.d("lkhai4617", "handleStatus: orderstatusfragment " + apiResult.getMessage());
                        CustomToast.showToast(requireContext(), apiResult.getMessage(), 2000);
                        break;
                }
            }
        });
    }

    private void setupListener(){
        orderAdminAdapter.setListener(new OrderAdminAdapter.IOnClickOrder() {
            @Override
            public void onClickUpdateStatus(OrderAdminResponse orderAdminResponse) {
                UpdateOrderStatusDialogFragment fragment = UpdateOrderStatusDialogFragment.newInstance(orderAdminResponse);
                fragment.setListener(new UpdateOrderStatusDialogFragment.IOnStatusUpdatedListener() {
                    @Override
                    public void onStatusUpdated(OrderStatusEnum newStatus) {
                        Log.d("lkhai4617", "onStatusUpdated: " + orderAdminResponse.getAddress().getId());
                        orderManagementViewModel.updateOrder(new UpdateOrderRequest(orderAdminResponse.getId(), newStatus.toString(), orderAdminResponse.getAddress()));
                    }
                });

                fragment.show(requireActivity().getSupportFragmentManager(), "update_order_status_dialog_fragment");
            }
        });
    }
}