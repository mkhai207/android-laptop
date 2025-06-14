package com.example.android_doan.view.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android_doan.R;
import com.example.android_doan.adapter.OrderAdminAdapter;
import com.example.android_doan.base.BaseViewModelFactory;
import com.example.android_doan.data.enums.OrderStatusEnum;
import com.example.android_doan.data.model.request.UpdateOrderRequest;
import com.example.android_doan.data.model.response.OrderAdminResponse;
import com.example.android_doan.data.repository.RemoteRepository.OrderManagementRepository;
import com.example.android_doan.databinding.FragmentOrderStatusBinding;
import com.example.android_doan.utils.CustomToast;
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
        if (args != null) {
            mStatus = (String) args.get("status");
        }

        orderManagementViewModel =
                new ViewModelProvider(
                        this,
                        new BaseViewModelFactory<OrderManagementRepository>(new OrderManagementRepository(), OrderManagementViewModel.class)
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
    public void onResume() {
        super.onResume();
        orderManagementViewModel.getAllOrder(mStatus);
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

    private void observer() {
        orderManagementViewModel.getOrdersLiveData()
                .observe(getViewLifecycleOwner(), orderAdminResponses -> {
                    orderAdminAdapter.updateData(orderAdminResponses);
                });
        orderManagementViewModel.getAllOrder(mStatus);
    }

    private void handleStatus() {
        orderManagementViewModel.getApiResultLiveData().observe(getViewLifecycleOwner(), apiResult -> {
            if (apiResult != null) {
                switch (apiResult.getStatus()) {
                    case LOADING:
                        binding.progressBar.setVisibility(View.VISIBLE);
                        break;
                    case SUCCESS:
                        binding.progressBar.setVisibility(View.GONE);
                        switch (apiResult.getMessage()) {
                            case "updateOrder":
                                orderManagementViewModel.refresh(mStatus);
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

    private void setupListener() {
        orderAdminAdapter.setListener(new OrderAdminAdapter.IOnClickOrder() {
            @Override
            public void onClickUpdateStatus(OrderAdminResponse orderAdminResponse) {
                UpdateOrderStatusDialogFragment fragment = UpdateOrderStatusDialogFragment.newInstance(orderAdminResponse);
                fragment.setListener(new UpdateOrderStatusDialogFragment.IOnStatusUpdatedListener() {
                    @Override
                    public void onStatusUpdated(OrderStatusEnum newStatus) {
                        orderManagementViewModel.updateOrder(new UpdateOrderRequest(orderAdminResponse.getId(), newStatus.toString(), orderAdminResponse.getAddress()));
                    }
                });

                fragment.show(requireActivity().getSupportFragmentManager(), "update_order_status_dialog_fragment");
            }

            @Override
            public void onClickViewDetail(OrderAdminResponse orderAdminResponse) {
                Bundle args = new Bundle();
                args.putSerializable(OrderDetailFragment.ORDER_DETAILS, orderAdminResponse.mapToOrderData());

                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
                navController.navigate(R.id.action_orderFragment_to_orderDetailFragment, args);
            }
        });
    }
}