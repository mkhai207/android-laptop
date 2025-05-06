package com.example.android_doan.view.fragment;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.android_doan.R;
import com.example.android_doan.data.enums.OrderStatusEnum;
import com.example.android_doan.data.model.response.OrderAdminResponse;
import com.example.android_doan.databinding.BottomDialogUpdateOrderStatusBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class UpdateOrderStatusDialogFragment extends BottomSheetDialogFragment {
    public static final String ORDER_KEY = "com.example.android_doan.view.fragment.ORDER_KEY";
    private BottomDialogUpdateOrderStatusBinding binding;
    private OrderAdminResponse mOrder;
    private IOnStatusUpdatedListener listener;

    public interface IOnStatusUpdatedListener {
        void onStatusUpdated(OrderStatusEnum newStatus);
    }

    public void setListener(IOnStatusUpdatedListener listener) {
        this.listener = listener;
    }

    public static UpdateOrderStatusDialogFragment newInstance(OrderAdminResponse order) {
        Bundle args = new Bundle();
        args.putSerializable(ORDER_KEY, order);
        UpdateOrderStatusDialogFragment fragment = new UpdateOrderStatusDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.DialogTheme);
        if (getArguments() != null) {
            mOrder = (OrderAdminResponse) getArguments().getSerializable(ORDER_KEY);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = BottomDialogUpdateOrderStatusBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupSpinner();
        setupListener();

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    private void setupSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item,
                OrderStatusEnum.getAllStatusNames());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerStatus.setAdapter(adapter);

        if (mOrder != null && mOrder.getStatus() != null) {
            int position = adapter.getPosition(mOrder.getStatus());
            binding.spinnerStatus.setSelection(position);
        }
    }

    private void setupListener() {
        binding.btnUpdate.setOnClickListener(v -> {
            String selectedStatus = (String) binding.spinnerStatus.getSelectedItem();
            if (selectedStatus != null) {
                OrderStatusEnum newStatus = OrderStatusEnum.valueOf(selectedStatus);
                if (listener != null) {
                    listener.onStatusUpdated(newStatus);
                }
                dismiss();
            }
        });

        binding.btnCancel.setOnClickListener(v -> dismiss());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
