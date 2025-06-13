package com.example.android_doan.view.fragment;

import static androidx.navigation.Navigation.findNavController;

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
import androidx.viewpager2.widget.ViewPager2;

import com.example.android_doan.R;
import com.example.android_doan.adapter.OrderViewPager2Adapter;
import com.example.android_doan.base.BaseViewModelFactory;
import com.example.android_doan.data.enums.OrderStatusEnum;
import com.example.android_doan.data.repository.RemoteRepository.OrderManagementRepository;
import com.example.android_doan.databinding.FragmentOrderManagementBinding;
import com.example.android_doan.utils.CustomToast;
import com.example.android_doan.viewmodel.OrderManagementViewModel;
import com.google.android.material.tabs.TabLayoutMediator;

public class OrderManagementFragment extends Fragment {
    private FragmentOrderManagementBinding binding;
    private OrderManagementViewModel orderManagementViewModel;
    private OrderViewPager2Adapter orderViewPager2Adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        orderManagementViewModel =
                new ViewModelProvider(
                        this,
                        new BaseViewModelFactory<OrderManagementRepository>(new OrderManagementRepository(), OrderManagementViewModel.class)
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
        handleStatus();
        setupListener();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    private void setupViewPager() {
        orderViewPager2Adapter = new OrderViewPager2Adapter(this);
        binding.viewPager.setAdapter(orderViewPager2Adapter);
        binding.viewPager.setOffscreenPageLimit(1);

        new TabLayoutMediator(binding.tabLayout, binding.viewPager, (tab, position) -> {
            tab.setText(OrderStatusEnum.values()[position].name());
        }).attach();

        binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                String status = OrderStatusEnum.values()[position].name();
                Log.d("OrderManagementFragment", "Tab selected: " + status);
                orderManagementViewModel.getAllOrder(status);
            }
        });
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
                                orderViewPager2Adapter.notifyDataSetChanged();
                                break;
                        }
                        break;
                    case ERROR:
                        binding.progressBar.setVisibility(View.GONE);
                        Log.d("lkhai4617", "handleStatus: orderStatusFragment " + apiResult.getMessage());
                        CustomToast.showToast(requireContext(), apiResult.getMessage(), 2000);
                        break;
                }
            }
        });
    }

    private void setupListener() {
        binding.btnCreateOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = findNavController(v);
                navController.navigate(R.id.action_orderFragment_to_addOrderFragment);
            }
        });
    }
}