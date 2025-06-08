package com.example.android_doan.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.android_doan.base.BaseViewModelFactory;
import com.example.android_doan.data.model.BrandModel;
import com.example.android_doan.data.model.request.CreateBrandRequest;
import com.example.android_doan.data.repository.RemoteRepository.BrandManagementRepository;
import com.example.android_doan.databinding.FragmentAddOrUpdateBrandBinding;
import com.example.android_doan.utils.CustomToast;
import com.example.android_doan.viewmodel.BrandManagementViewModel;

public class AddOrUpdateBrandFragment extends Fragment {
    public static final String BRAND_MODEL_KEY = "com.example.android_doan.view.fragment.BRAND_MODEL_KEY";
    private BrandModel brandModel;
    private BrandManagementViewModel brandManagementViewModel;
    private FragmentAddOrUpdateBrandBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            brandModel = (BrandModel) args.getSerializable(BRAND_MODEL_KEY);
        }

        brandManagementViewModel = new ViewModelProvider(
                this,
                new BaseViewModelFactory<BrandManagementRepository>(new BrandManagementRepository(), BrandManagementViewModel.class)
        ).get(BrandManagementViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddOrUpdateBrandBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupData();
        setupListener();
        handleStatus();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    private void setupData() {
        if (brandModel != null) {
            binding.etName.setText(brandModel.getName());
        }
    }

    private void setupListener() {
        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (brandModel != null) {
                    updateBrand();
                } else {
                    createBrand();
                }
            }
        });
    }

    private void handleStatus() {
        brandManagementViewModel.getApiResultLiveData().observe(getViewLifecycleOwner(), apiResult -> {
            if (apiResult != null) {
                switch (apiResult.getStatus()) {
                    case LOADING:
                        binding.progressBar.setVisibility(View.VISIBLE);
                        break;
                    case SUCCESS:
                        binding.progressBar.setVisibility(View.GONE);
                        switch (apiResult.getMessage()) {
                            case "createBrand":
                                requireActivity().getSupportFragmentManager().popBackStack();
                                break;
                            case "updateBrand":
                                requireActivity().getSupportFragmentManager().popBackStack();
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

    private void updateBrand() {
        String name = binding.etName.getText().toString();
        int id = brandModel.getId();
        brandManagementViewModel.updateBrand(new BrandModel(id, name));
    }

    private void createBrand() {
        String name = binding.etName.getText().toString();
        brandManagementViewModel.createBrand(new CreateBrandRequest(name));
    }
}