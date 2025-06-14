package com.example.android_doan.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android_doan.adapter.OrderDetailAdapter;
import com.example.android_doan.data.model.OrderData;
import com.example.android_doan.databinding.FragmentOrderDetailBinding;
import com.example.android_doan.utils.FormatUtil;

import java.util.ArrayList;

public class OrderDetailFragment extends Fragment {
    public static final String ORDER_DETAILS = "com.example.android_doan.view.fragment.ORDER_DETAILS";
    private FragmentOrderDetailBinding binding;
    private RecyclerView rcvOrderDetail;
    private OrderDetailAdapter orderDetailAdapter;
    private OrderData mData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mData = (OrderData) args.getSerializable(ORDER_DETAILS);
        }

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentOrderDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupRcv();
        setupData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    private void setupRcv() {
        rcvOrderDetail = binding.rcvOrderItems;
        orderDetailAdapter = new OrderDetailAdapter(new ArrayList<>(mData.getOrderDetails()));
        rcvOrderDetail.setAdapter(orderDetailAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        rcvOrderDetail.setLayoutManager(linearLayoutManager);
    }

    private void setupData() {
        String orderId = "Đơn hàng: #" + mData.getId();
        binding.tvOrderId.setText(orderId);
        binding.tvOrderStatus.setText(mData.getStatus());
        String date = FormatUtil.formatIsoDate(mData.getCreatedAt());
        binding.tvOrderDate.setText(date);
        String productCount = mData.getOrderDetails().size() + " sản phẩm";
        binding.tvProductCount.setText(productCount);
        binding.tvAddress.setText(mData.getShippingAddress());
        binding.tvPaymentMethod.setText(mData.getPaymentMethod());
    }
}