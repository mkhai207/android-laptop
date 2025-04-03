package com.example.android_doan.view.fragment;


import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.example.android_doan.data.model.Address;
import com.example.android_doan.databinding.BottomDialogEdtAddressBinding;
import com.example.android_doan.viewmodel.CheckoutViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class EdtAddressBottomSheetFragment extends BottomSheetDialogFragment {
    public static final String ADDRESS_KEY = "com.example.android_doan.view.fragment.ADDRESS_KEY";

    private CheckoutViewModel checkoutViewModel;
    private BottomDialogEdtAddressBinding binding;
    private Address mAddress;

//    private IOnClickSave listener;
//    public interface IOnClickSave{
//        void onClickSave(Address address);
//    }
//
//    public void setListener(IOnClickSave listener) {
//        this.listener = listener;
//    }

    public static EdtAddressBottomSheetFragment newInstance(Address address){
        Bundle args = new Bundle();
        args.putSerializable(ADDRESS_KEY, address);

        EdtAddressBottomSheetFragment fragment = new EdtAddressBottomSheetFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
//        if (bundle != null){
//            mAddress = (Address) bundle.getSerializable(ADDRESS_KEY);
//        }

        checkoutViewModel = new ViewModelProvider(requireActivity()).get(CheckoutViewModel.class);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = BottomDialogEdtAddressBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupData();
        setupListener();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    private void setupData(){
        checkoutViewModel.getAddress().observe(getViewLifecycleOwner(), address -> {
            if (address != null) {
                binding.edtReceiverName.setText(address.getName());
                binding.edtPhone.setText(address.getPhone());
                binding.edtAddressDetail.setText(address.getAddress());
            }
        });
    }

    private void setupListener(){
        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = binding.edtReceiverName.getText().toString();
                String phone = binding.edtPhone.getText().toString();
                String address = binding.edtAddressDetail.getText().toString();
                Address newAddress = new Address(name, phone, address);
                checkoutViewModel.setAddress(newAddress);
                Log.d("lkhai4617", "btn clicked");
                dismiss();
            }
        });

        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }
}
