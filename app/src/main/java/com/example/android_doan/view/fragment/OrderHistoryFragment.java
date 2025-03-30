package com.example.android_doan.view.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android_doan.R;
import com.example.android_doan.adapter.OrderAdapter;
import com.example.android_doan.data.repository.LocalRepository.DataLocalManager;
import com.example.android_doan.data.repository.RemoteRepository.OrderRepository;
import com.example.android_doan.databinding.FragmentOrderHistoryBinding;
import com.example.android_doan.viewmodel.OrderViewModel;
import com.example.android_doan.viewmodel.OrderViewModelFactory;

import java.util.ArrayList;

public class OrderHistoryFragment extends Fragment {
    private FragmentOrderHistoryBinding binding;
    private OrderRepository repository;
    private OrderViewModel orderViewModel;
    private RecyclerView rcvOrders;
    private OrderAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        repository = new OrderRepository();
        orderViewModel = new ViewModelProvider(this, new OrderViewModelFactory(repository)).get(OrderViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentOrderHistoryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupRcv();
        getOrders();
        handleStatus();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding =null;
    }

    private void setupRcv(){
        rcvOrders = binding.rcvOrders;
        adapter = new OrderAdapter(new ArrayList<>());
        rcvOrders.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        rcvOrders.setLayoutManager(linearLayoutManager);
    }

    private void getOrders(){
        String userId = DataLocalManager.getUserId();
        orderViewModel.getOrders(userId);
        orderViewModel.getOrderLiveData().observe(getViewLifecycleOwner(), orderData -> {
            if (orderData != null){
                adapter.updateData(orderData);
            }
        });
    }

    private void handleStatus(){

    }
}