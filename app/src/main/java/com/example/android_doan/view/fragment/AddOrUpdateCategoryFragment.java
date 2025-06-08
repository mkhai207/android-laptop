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
import com.example.android_doan.data.model.CategoryModel;
import com.example.android_doan.data.model.request.CreateCategoryRequest;
import com.example.android_doan.data.model.request.UpdateCategoryRequest;
import com.example.android_doan.data.repository.RemoteRepository.CategoryManagementRepository;
import com.example.android_doan.databinding.FragmentAddOrUpdateCategoryBinding;
import com.example.android_doan.utils.CustomToast;
import com.example.android_doan.viewmodel.CategoryManagementViewModel;

public class AddOrUpdateCategoryFragment extends Fragment {
    public static final String CATEGORY_MODEL_KEY = "com.example.android_doan.view.fragment.CATEGORY_MODEL_KEY";
    private FragmentAddOrUpdateCategoryBinding binding;
    private CategoryManagementViewModel categoryManagementViewModel;
    private CategoryModel mCategoryModel;

    public static AddOrUpdateCategoryFragment newInstance(CategoryModel categoryModel) {
        AddOrUpdateCategoryFragment fragment = new AddOrUpdateCategoryFragment();
        if (categoryModel != null) {
            Bundle args = new Bundle();
            args.putSerializable(CATEGORY_MODEL_KEY, categoryModel);

            fragment.setArguments(args);
        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mCategoryModel = (CategoryModel) args.getSerializable(CATEGORY_MODEL_KEY);
        }

        categoryManagementViewModel =
                new ViewModelProvider(
                        this,
                        new BaseViewModelFactory<CategoryManagementRepository>(new CategoryManagementRepository(), CategoryManagementViewModel.class)
                ).get(CategoryManagementViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddOrUpdateCategoryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupData();
        setupListener();
        observer();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    private void setupData() {
        if (mCategoryModel != null) {
            binding.etName.setText(mCategoryModel.getName());
            binding.etCode.setText(mCategoryModel.getCode());
        }
    }

    private void setupListener() {
        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCategoryModel != null) {
                    updateCategory();
                } else {
                    createCategory();
                }
            }
        });
    }

    private void observer() {
        categoryManagementViewModel.getCategoryLiveData().observe(getViewLifecycleOwner(), categoryModel -> {

        });

        categoryManagementViewModel.getApiResultLiveData().observe(getViewLifecycleOwner(), apiResult -> {
            if (apiResult != null) {
                switch (apiResult.getStatus()) {
                    case LOADING:
                        binding.progressBar.setVisibility(View.VISIBLE);
                        break;
                    case SUCCESS:
                        binding.progressBar.setVisibility(View.GONE);
                        switch (apiResult.getMessage()) {
                            case "createCategory":
                                requireActivity().getSupportFragmentManager().popBackStack();
                                break;
                            case "updateCategory":
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

    private void createCategory() {
        String code = binding.etCode.getText().toString();
        String name = binding.etName.getText().toString();
        CreateCategoryRequest request = new CreateCategoryRequest(name, code);
        categoryManagementViewModel.createCategory(request);
    }

    private void updateCategory() {
        String code = binding.etCode.getText().toString();
        String name = binding.etName.getText().toString();
        UpdateCategoryRequest request = new UpdateCategoryRequest(Integer.parseInt(mCategoryModel.getId()), name, code);
        categoryManagementViewModel.updateCategory(request);
    }
}