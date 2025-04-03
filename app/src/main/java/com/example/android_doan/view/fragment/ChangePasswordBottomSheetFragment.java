package com.example.android_doan.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.android_doan.databinding.ChangePasswordBottomSheetFragmentBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class ChangePasswordBottomSheetFragment extends BottomSheetDialogFragment {
    private ChangePasswordBottomSheetFragmentBinding binding;
    private  IOnCLickSavePassword listener;
    public interface IOnCLickSavePassword{
        void onClickSavePassword(String oldPassword ,String newPassword);
    }
    public static ChangePasswordBottomSheetFragment newInstance(IOnCLickSavePassword listener){
        ChangePasswordBottomSheetFragment fragment = new ChangePasswordBottomSheetFragment();
        fragment.listener = listener;
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ChangePasswordBottomSheetFragmentBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.btnSavePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newPassword = binding.edtNewPassword.getText().toString();
                String confirmNewPassword = binding.edtConfirmPassword.getText().toString();
                String oldPassword = binding.edtCurrentPassword.getText().toString();
                if (!newPassword.equals(oldPassword) && newPassword.equals(confirmNewPassword)){
                    if (listener != null){
                        listener.onClickSavePassword(oldPassword, newPassword);
                        dismiss();
                    }
                }
            }
        });
    }
}
