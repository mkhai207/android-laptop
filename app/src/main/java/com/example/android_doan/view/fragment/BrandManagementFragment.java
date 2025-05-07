package com.example.android_doan.view.fragment;

import static com.example.android_doan.view.fragment.AddOrUpdateBrandFragment.BRAND_MODEL_KEY;
import static com.example.android_doan.view.fragment.AddOrUpdateCategoryFragment.CATEGORY_MODEL_KEY;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android_doan.R;
import com.example.android_doan.adapter.BrandAdapter;
import com.example.android_doan.adapter.CategoryAdapter;
import com.example.android_doan.data.model.BrandModel;
import com.example.android_doan.data.model.response.AddressResponse;
import com.example.android_doan.data.repository.RemoteRepository.BrandManagementRepository;
import com.example.android_doan.data.repository.RemoteRepository.CategoryManagementRepository;
import com.example.android_doan.databinding.FragmentBrandManagementBinding;
import com.example.android_doan.databinding.FragmentCategoryManagementBinding;
import com.example.android_doan.utils.CustomToast;
import com.example.android_doan.viewmodel.BrandManagementViewModel;
import com.example.android_doan.viewmodel.BrandManagementViewModelFactory;
import com.example.android_doan.viewmodel.CategoryManagementViewModel;
import com.example.android_doan.viewmodel.CategoryManagementViewModelFactory;

import java.util.ArrayList;

public class BrandManagementFragment extends Fragment {
    private FragmentBrandManagementBinding binding;
    private RecyclerView rcvBrand;
    private BrandAdapter brandAdapter;
    private BrandManagementViewModel brandManagementViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BrandManagementRepository brandManagementRepository = new BrandManagementRepository();
        brandManagementViewModel = new ViewModelProvider(
                this,
                new BrandManagementViewModelFactory(brandManagementRepository)
        ).get(BrandManagementViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBrandManagementBinding.inflate(inflater, container, false);

        setupRecyclerView();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getAllBrand();
        handleStatus();
        setupListener();
    }

    @Override
    public void onResume() {
        super.onResume();
        brandManagementViewModel.refresh();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    private void setupRecyclerView(){
        rcvBrand = binding.rcvBrand;
        brandAdapter = new BrandAdapter(new ArrayList<>());
        rcvBrand.setAdapter(brandAdapter);

        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        rcvBrand.setLayoutManager(linearLayoutManager);

        brandAdapter.attachSwipeToDelete(rcvBrand);
    }

    private void handleStatus(){
        brandManagementViewModel.getApiResultLiveData().observe(getViewLifecycleOwner(), apiResult -> {
            if (apiResult != null){
                switch (apiResult.getStatus()){
                    case LOADING:
                        binding.progressBar.setVisibility(View.VISIBLE);
                        break;
                    case SUCCESS:
                        binding.progressBar.setVisibility(View.GONE);
                        switch (apiResult.getMessage()){
                            case "deleteBrand":
                                brandManagementViewModel.refresh();
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

    private void getAllBrand(){
        brandManagementViewModel.getBrand();
        brandManagementViewModel.getBrandsLiveData().observe(getViewLifecycleOwner(), brands -> {
            if (brands != null && !brands.isEmpty()){
                brandAdapter.updateData(brands);
                Log.d("lkhai4617", "getAllBrand: oke " + brands.size());
            }
        });
    }

    private void setupListener(){
        binding.btnAddBrand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddOrUpdateBrandFragment(null);
            }
        });

        brandAdapter.setListener(new BrandAdapter.IOnClickBrand() {
            @Override
            public void onClickItemBrand() {

            }

            @Override
            public void onClickEdit(BrandModel brandModel) {
                openAddOrUpdateBrandFragment(brandModel);
            }

            @Override
            public void onCLickDelete(BrandModel brandModel) {
                showLogoutConfirmationDialog(brandModel);
            }
        });
    }

    private void openAddOrUpdateBrandFragment(BrandModel brandModel){
        NavHostFragment navHostFragment =
                (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        if (navHostFragment != null){
            if (brandModel != null){
                Bundle args = new Bundle();
                args.putSerializable(BRAND_MODEL_KEY, brandModel);

                NavController navController = navHostFragment.getNavController();
                navController.navigate(R.id.action_brandManagementFragment_to_addOrUpdateBrandFragment, args);
            } else {
                NavController navController = navHostFragment.getNavController();
                navController.navigate(R.id.action_brandManagementFragment_to_addOrUpdateBrandFragment);
            }
        }
    }

    private void deleteBrand(BrandModel brandModel){
        if (brandModel != null){
            brandManagementViewModel.deleteBrand(brandModel.getId());
        }
    }

    private void showLogoutConfirmationDialog(BrandModel brandModel) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Xác nhận xoá");
        builder.setMessage("Bạn có chắc chắn muốn xoá hãng này không?");
        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                brandManagementViewModel.deleteBrand(brandModel.getId());
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