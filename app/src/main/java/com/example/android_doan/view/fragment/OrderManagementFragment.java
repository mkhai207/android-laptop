package com.example.android_doan.view.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android_doan.R;
import com.example.android_doan.adapter.OrderViewPager2Adapter;
import com.example.android_doan.data.enums.OrderStatusEnum;
import com.example.android_doan.data.repository.RemoteRepository.OrderManagementRepository;
import com.example.android_doan.databinding.FragmentOrderManagementBinding;
import com.example.android_doan.utils.CustomToast;
import com.example.android_doan.viewmodel.OrderManagementVIewModelFactory;
import com.example.android_doan.viewmodel.OrderManagementViewModel;
import com.google.android.material.tabs.TabLayoutMediator;

public class OrderManagementFragment extends Fragment {
    private FragmentOrderManagementBinding binding;
    private OrderManagementViewModel orderManagementViewModel;
    private OrderViewPager2Adapter orderViewPager2Adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        binding = FragmentOrderManagementBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupViewPager();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    private void setupViewPager(){
        orderViewPager2Adapter = new OrderViewPager2Adapter(this);
        binding.viewPager.setAdapter(orderViewPager2Adapter);

        new TabLayoutMediator(binding.tabLayout, binding.viewPager, (tab, position) -> {
            tab.setText(OrderStatusEnum.values()[position].name());
        }).attach();
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
                                orderViewPager2Adapter.notifyDataSetChanged();
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
}