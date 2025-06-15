package com.example.android_doan.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android_doan.R;
import com.example.android_doan.adapter.OrderDetailAdapter;
import com.example.android_doan.base.BaseViewModelFactory;
import com.example.android_doan.data.enums.OrderStatusEnum;
import com.example.android_doan.data.enums.RoleEnum;
import com.example.android_doan.data.model.OrderData;
import com.example.android_doan.data.model.request.UpdateOrderRequest;
import com.example.android_doan.data.repository.LocalRepository.DataLocalManager;
import com.example.android_doan.data.repository.RemoteRepository.OrderManagementRepository;
import com.example.android_doan.databinding.FragmentOrderDetailBinding;
import com.example.android_doan.utils.CustomToast;
import com.example.android_doan.utils.FormatUtil;
import com.example.android_doan.viewmodel.OrderManagementViewModel;

import java.util.ArrayList;

public class OrderDetailFragment extends Fragment {
    public static final String ORDER_DETAILS = "com.example.android_doan.view.fragment.ORDER_DETAILS";
    private FragmentOrderDetailBinding binding;
    private RecyclerView rcvOrderDetail;
    private OrderDetailAdapter orderDetailAdapter;
    private OrderData mData;
    private OrderManagementViewModel orderManagementViewModel;

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
        orderManagementViewModel =
                new ViewModelProvider(
                        this,
                        new BaseViewModelFactory<OrderManagementRepository>(new OrderManagementRepository(), OrderManagementViewModel.class)
                ).get(OrderManagementViewModel.class);

        binding = FragmentOrderDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        handleStatus();
        initUI();
        setupRcv();
        setupData();
        setupListener();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
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
                                binding.edtRecipientName.setEnabled(false);
                                binding.edtAddress.setEnabled(false);
                                binding.edtPhoneNumber.setEnabled(false);
                                binding.btnEditRecipient.setVisibility(View.VISIBLE);
                                binding.btnSave.setVisibility(View.GONE);
                                CustomToast.showToast(requireContext(), "Thành công!", Toast.LENGTH_SHORT);
                                break;
                        }
                        break;
                    case ERROR:
                        binding.progressBar.setVisibility(View.GONE);
                        CustomToast.showToast(requireContext(), apiResult.getMessage(), 2000);
                        break;
                }
            }
        });
    }

    private void initUI() {
        if (OrderStatusEnum.fromString(mData.getStatus()).equals(OrderStatusEnum.PENDING)
                && !RoleEnum.fromString(DataLocalManager.getRole()).equals(RoleEnum.CUSTOMER)) {
            binding.btnEditRecipient.setVisibility(View.VISIBLE);
        } else {
            binding.btnEditRecipient.setVisibility(View.GONE);
        }
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
        binding.edtAddress.setText(mData.getShippingAddress());
        binding.tvPaymentMethod.setText(mData.getPaymentMethod());
        binding.edtRecipientName.setText(mData.getName());
        binding.edtPhoneNumber.setText(mData.getPhone());
    }

    private void setupListener() {
        binding.btnEditRecipient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.edtRecipientName.setEnabled(true);
                binding.edtRecipientName.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.bg_white_corner_15));
                binding.edtAddress.setEnabled(true);
                binding.edtAddress.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.bg_white_corner_15));
                binding.edtPhoneNumber.setEnabled(true);
                binding.edtPhoneNumber.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.bg_white_corner_15));
                binding.btnSave.setVisibility(View.VISIBLE);
                binding.btnEditRecipient.setVisibility(View.GONE);

            }
        });

        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String recipientName = binding.edtRecipientName.getText().toString().trim();
                String phone = binding.edtPhoneNumber.getText().toString().trim();
                String shippingAddress = binding.edtAddress.getText().toString().trim();

                if (recipientName.isBlank()) {
                    CustomToast.showToast(requireContext(), "Vui lòng nhập tên người nhân!", Toast.LENGTH_SHORT);
                }

                if (phone.isBlank()) {
                    CustomToast.showToast(requireContext(), "Vui lòng nhập SĐT người nhân!", Toast.LENGTH_SHORT);
                }

                if (shippingAddress.isBlank()) {
                    CustomToast.showToast(requireContext(), "Vui lòng nhập địa chỉ nhận hàng!", Toast.LENGTH_SHORT);
                }

                UpdateOrderRequest request = new UpdateOrderRequest(mData.getId(), mData.getStatus());
                request.setName(recipientName);
                request.setPhone(phone);
                request.setShippingAddress(shippingAddress);
                orderManagementViewModel.updateOrder(request);
            }
        });
    }
}