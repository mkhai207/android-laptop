package com.example.android_doan.view.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.android_doan.data.model.UserModel;
import com.example.android_doan.data.repository.LocalRepository.DataLocalManager;
import com.example.android_doan.data.repository.RemoteRepository.SettingRepository;
import com.example.android_doan.databinding.FragmentSettingBinding;
import com.example.android_doan.utils.FormatUtil;
import com.example.android_doan.viewmodel.SettingViewModel;
import com.example.android_doan.viewmodel.SettingViewModelFactory;

import java.util.ArrayList;
import java.util.List;

public class SettingFragment extends Fragment {
    private FragmentSettingBinding binding;
    private SettingViewModel settingViewModel;
    private List<String> genders;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSettingBinding.inflate(inflater, container, false);
        SettingRepository repository = new SettingRepository();
        settingViewModel = new ViewModelProvider(requireActivity(), new SettingViewModelFactory(repository)).get(SettingViewModel.class);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI();
        loadData();
        observer();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
    private void initUI(){
        Spinner spinnerGender = binding.spinnerGender;
        genders = new ArrayList<>();
        genders.add("MALE");
        genders.add("FEMALE");
        genders.add("OTHER");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, genders);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(adapter);

    }

    private void loadData(){
        String userId = DataLocalManager.getUserId();
        if (userId == null || userId.isEmpty()){
            return;
        }
        settingViewModel.getUser(userId);
    }

    private void observer(){
        settingViewModel.getUserInfo().observe(getViewLifecycleOwner(), userResponse -> {
            if (userResponse != null && userResponse.getData() != null) {
                bindData(userResponse.getData());
            }
        });
    }

    private void bindData(UserModel userModel){
        if (userModel.getFullName() != null) {
            binding.edtFullName.setText(userModel.getFullName());
        }

        if (userModel.getAddress() != null){
            binding.edtAddress.setText(userModel.getAddress());
        }

        if (userModel.getBirthday() != null){
            binding.edtBirthday.setText(FormatUtil.formatIsoDate(userModel.getBirthday()));
        }

        if (userModel.getGender() != null){
            int position = genders.indexOf(userModel.getGender());
            if (position != -1){
                binding.spinnerGender.setSelection(position);
            }
        }

        if (userModel.getPhone() != null){
            binding.edtPhone.setText(userModel.getPhone());
        }

        if (userModel.getShoppingAddress() != null){
            binding.edtShoppingAddress.setText(userModel.getShoppingAddress());
        }
    }
}