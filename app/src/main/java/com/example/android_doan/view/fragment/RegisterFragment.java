package com.example.android_doan.view.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.example.android_doan.R;
import com.example.android_doan.data.model.request.RegisterRequest;
import com.example.android_doan.databinding.FragmentRegisterBinding;
import com.example.android_doan.viewmodel.AuthViewModel;

public class RegisterFragment extends Fragment {
    private Context context;
    private AuthViewModel authViewModel;
    private FragmentRegisterBinding binding;
    private final String[] mListGender = {"MALE", "FEMALE", "OTHER"};
    private String gender;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);

        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        initSpinner();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        NavController navController = Navigation.findNavController(requireActivity(), R.id.frg_container);

        binding.btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.loginFragment);
            }
        });

        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fullName = binding.edtName.getText().toString();
                String email = binding.edtEmail.getText().toString();
                String password = binding.edtPassword.getText().toString();
                String confirmPassword = binding.edtConfirmPassword.getText().toString();
                if (!TextUtils.isEmpty(password) && !TextUtils.isEmpty(confirmPassword) && confirmPassword.equals(password)){
                    RegisterRequest registerRequest = new RegisterRequest(fullName, email, password, gender);
                    authViewModel.register(registerRequest);
                }
            }
        });
    }

    private void initSpinner(){
        ArrayAdapter arrayAdapter = new ArrayAdapter(requireContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, mListGender);
        binding.spinnerGender.setAdapter(arrayAdapter);

        binding.spinnerGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                gender = adapterView.getItemAtPosition(i).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                gender = "MALE";
            }
        });
    }
}