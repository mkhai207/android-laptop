package com.example.android_doan.view.fragment;

import static com.example.android_doan.view.fragment.AddOrUpdateProductFragment.PRODUCT_MODEL_KEY;

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
import com.example.android_doan.adapter.ProductAdminAdapter;
import com.example.android_doan.base.BaseViewModelFactory;
import com.example.android_doan.data.model.ProductModel;
import com.example.android_doan.data.repository.RemoteRepository.ProductManagementRepository;
import com.example.android_doan.databinding.FragmentProductManagementBinding;
import com.example.android_doan.utils.Resource;
import com.example.android_doan.viewmodel.ProductManagementViewModel;

import java.util.ArrayList;

public class ProductManagementFragment extends Fragment {
    public static final String REQUEST_KEY_PRODUCT = "com.example.android_doan.view.fragment.REQUEST_KEY_PRODUCT";
    public static final String PRODUCT_KEY = "com.example.android_doan.view.fragment.PRODUCT_KEY";
    private FragmentProductManagementBinding binding;
    private ProductManagementViewModel productManagementViewModel;
    private ProductAdminAdapter productAdminAdapter;
    private RecyclerView rcvProduct;
    private Boolean mActionBack = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("lkhai4617", "onCreate: ProductManagementFragment");
        productManagementViewModel = new ViewModelProvider(
                this,
                new BaseViewModelFactory<ProductManagementRepository>(new ProductManagementRepository(), ProductManagementViewModel.class)
        ).get(ProductManagementViewModel.class);

        if (getArguments() != null) {
            mActionBack = getArguments().getBoolean("ACTION_BACK_KEY");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("lkhai4617", "onCreateView: ProductManagementFragment");
        binding = FragmentProductManagementBinding.inflate(inflater, container, false);
        setupRecyclerView();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("lkhai4617", "onViewCreated: ProductManagementFragment");
        productManagementViewModel.refresh();
        getAllProduct();
        handleStatus();
        setupListener();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("lkhai4617", "onResume: ProductManagementFragment");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("lkhai4617", "onDestroy: ProductManagementFragment");
        binding = null;
    }

    private void setupRecyclerView() {
        rcvProduct = binding.rcvProduct;
        productAdminAdapter = new ProductAdminAdapter(new ArrayList<>());
        rcvProduct.setAdapter(productAdminAdapter);

        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        rcvProduct.setLayoutManager(linearLayoutManager);
    }

    private void getAllProduct() {
        productManagementViewModel.loadNextPage();
        productManagementViewModel.getProductsLiveData().observe(getViewLifecycleOwner(), products -> {
            if (!products.isEmpty()) {
                productAdminAdapter.updateData(products);
            }
        });

        rcvProduct.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

                        if (productManagementViewModel.getApiResultLiveData().getValue() != null && productManagementViewModel.getApiResultLiveData().getValue().getStatus() != Resource.Status.LOADING &&
                                (visibleItemCount + firstVisibleItemPosition) >= totalItemCount - 4) {
                            productManagementViewModel.loadNextPage();
                        }
                    }
                }
            }
        });
    }

    private void handleStatus() {
        productManagementViewModel.getApiResultLiveData().observe(getViewLifecycleOwner(), apiResult -> {
            if (apiResult != null) {
                switch (apiResult.getStatus()) {
                    case LOADING:
                        binding.progressBar.setVisibility(View.VISIBLE);
                        break;
                    case SUCCESS:
                        binding.progressBar.setVisibility(View.GONE);
                        switch (apiResult.getMessage()) {

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
        binding.btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddOrUpdateProductFragment(null);
            }
        });

        productAdminAdapter.setListener(new ProductAdminAdapter.IOnClickProduct() {

            @Override
            public void onClickEdit(ProductModel productModel) {
                openAddOrUpdateProductFragment(productModel);
            }

            @Override
            public void onCLickDelete(ProductModel productModel) {

            }

            @Override
            public void onClickSelect(ProductModel productModel) {
                Bundle result = new Bundle();
                result.putSerializable(PRODUCT_KEY, productModel);
                getParentFragmentManager().setFragmentResult(REQUEST_KEY_PRODUCT, result);
                NavHostFragment navHostFragment =
                        (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
                if (navHostFragment != null && mActionBack) {
                    NavController navController = navHostFragment.getNavController();
                    navController.popBackStack();
                }
            }
        });
    }

    private void openAddOrUpdateProductFragment(ProductModel productModel) {
        NavHostFragment navHostFragment =
                (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        if (navHostFragment != null) {
            if (productModel != null) {
                Bundle args = new Bundle();
                args.putSerializable(PRODUCT_MODEL_KEY, productModel);

                NavController navController = navHostFragment.getNavController();
                navController.navigate(R.id.action_productManagementFragment_to_addOrUpdateProductFragment, args);
            } else {
                NavController navController = navHostFragment.getNavController();
                navController.navigate(R.id.action_productManagementFragment_to_addOrUpdateProductFragment);
            }
        }
    }
}