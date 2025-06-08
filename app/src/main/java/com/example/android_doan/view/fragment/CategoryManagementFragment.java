package com.example.android_doan.view.fragment;

import static com.example.android_doan.view.fragment.AddOrUpdateCategoryFragment.CATEGORY_MODEL_KEY;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android_doan.R;
import com.example.android_doan.adapter.CategoryAdapter;
import com.example.android_doan.base.BaseViewModelFactory;
import com.example.android_doan.data.model.CategoryModel;
import com.example.android_doan.data.repository.RemoteRepository.CategoryManagementRepository;
import com.example.android_doan.databinding.FragmentCategoryManagementBinding;
import com.example.android_doan.utils.CustomToast;
import com.example.android_doan.utils.Resource;
import com.example.android_doan.viewmodel.CategoryManagementViewModel;

import java.util.ArrayList;

public class CategoryManagementFragment extends Fragment {
    private FragmentCategoryManagementBinding binding;
    private CategoryManagementViewModel categoryManagementViewModel;
    private RecyclerView rcvCategories;
    private CategoryAdapter categoryAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        categoryManagementViewModel = new ViewModelProvider(
                this,
                new BaseViewModelFactory<CategoryManagementRepository>(new CategoryManagementRepository(), CategoryManagementViewModel.class)
        ).get(CategoryManagementViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCategoryManagementBinding.inflate(inflater, container, false);

        setupRecyclerView();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getAllCategory();
        handleStatus();
        setupListener();
    }

    @Override
    public void onResume() {
        super.onResume();
        categoryManagementViewModel.refresh();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    private void setupRecyclerView() {
        rcvCategories = binding.rcvCategory;
        categoryAdapter = new CategoryAdapter(new ArrayList<>());
        rcvCategories.setAdapter(categoryAdapter);

        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        rcvCategories.setLayoutManager(linearLayoutManager);

        categoryAdapter.attachSwipeToDelete(rcvCategories);
    }

    private void getAllCategory() {
        categoryManagementViewModel.loadNextPage();
        categoryManagementViewModel.getCategoriesLiveData().observe(getViewLifecycleOwner(), categories -> {
            if (categories != null && !categories.isEmpty()) {
                categoryAdapter.updateData(categories);
                Log.d("lkhai4617", "getAllUser: oke " + categories.size());
            }
        });

        rcvCategories.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    if (layoutManager != null) {
                        int visibleItemCount = layoutManager.getChildCount();
                        int totalItemCount = layoutManager.getItemCount();
                        int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                        if (categoryManagementViewModel.getApiResultLiveData().getValue() != null && categoryManagementViewModel.getApiResultLiveData().getValue().getStatus() != Resource.Status.LOADING &&
                                (visibleItemCount + firstVisibleItemPosition) >= totalItemCount - 4) {
                            categoryManagementViewModel.loadNextPage();
                        }
                    }
                }
            }
        });
    }

    private void handleStatus() {
        categoryManagementViewModel.getApiResultLiveData().observe(getViewLifecycleOwner(), apiResult -> {
            if (apiResult != null) {
                switch (apiResult.getStatus()) {
                    case LOADING:
                        binding.progressBar.setVisibility(View.VISIBLE);
                        break;
                    case SUCCESS:
                        binding.progressBar.setVisibility(View.GONE);
                        switch (apiResult.getMessage()) {
                            case "deleteCategory":
                                categoryManagementViewModel.refresh();
                                CustomToast.showToast(requireContext(), "Thành công", 2000);
                                break;
                        }
                        break;
                    case ERROR:
                        binding.progressBar.setVisibility(View.GONE);
                        break;
                }
            }
        });
    }

    private void setupListener() {
        binding.btnAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddOrUpdateCategoryFragment(null);
            }
        });

        categoryAdapter.setListener(new CategoryAdapter.IOnClickCategory() {
            @Override
            public void onClickEdit(CategoryModel categoryModel) {
                openAddOrUpdateCategoryFragment(categoryModel);
            }

            @Override
            public void onCLickDelete(CategoryModel categoryModel) {
                showLogoutConfirmationDialog(categoryModel);
            }
        });
    }

    private void openAddOrUpdateCategoryFragment(CategoryModel categoryModel) {
        NavHostFragment navHostFragment =
                (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        if (navHostFragment != null) {
            if (categoryModel != null) {
                Bundle args = new Bundle();
                args.putSerializable(CATEGORY_MODEL_KEY, categoryModel);

                NavController navController = navHostFragment.getNavController();
                navController.navigate(R.id.action_categoryManagementFragment_to_addOrUpdateCategoryFragment, args);
            } else {
                NavController navController = navHostFragment.getNavController();
                navController.navigate(R.id.action_categoryManagementFragment_to_addOrUpdateCategoryFragment);
            }
        }
    }

    private void showLogoutConfirmationDialog(CategoryModel categoryModel) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Xác nhận xoá");
        builder.setMessage("Bạn có chắc chắn muốn xoá phân loại này không?");
        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                categoryManagementViewModel.deleteCategory(Integer.parseInt(categoryModel.getId()));
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setCancelable(true);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}