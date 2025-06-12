package com.example.android_doan.view.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.android_doan.R;
import com.example.android_doan.adapter.ProductAdapter;
import com.example.android_doan.base.BaseViewModelFactory;
import com.example.android_doan.data.model.BrandModel;
import com.example.android_doan.data.model.ProductModel;
import com.example.android_doan.data.model.SortOption;
import com.example.android_doan.data.repository.RemoteRepository.HomeRepository;
import com.example.android_doan.databinding.FragmentHomeBinding;
import com.example.android_doan.utils.GridSpacingItemDecoration;
import com.example.android_doan.viewmodel.HomeViewModel;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private HomeViewModel homeViewModel;
    private ProductAdapter productAdapter;
    private RecyclerView rcvProduct;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d("lkhai4617", "onCreate: HomeFragment");
        homeViewModel = new ViewModelProvider(
                this,
                new BaseViewModelFactory<HomeRepository>(new HomeRepository(), HomeViewModel.class)
        ).get(HomeViewModel.class);
//        homeViewModel.getSortLiveData().setValue("createdAt,desc");

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("lkhai4617", "onCreateView: HomeFragment");
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        rcvProduct = binding.rcvProduct;
        productAdapter = new ProductAdapter(requireContext(), new ArrayList<>());
        rcvProduct.setAdapter(productAdapter);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 2);
        rcvProduct.setLayoutManager(gridLayoutManager);

        GridSpacingItemDecoration gridSpacingItemDecoration = new GridSpacingItemDecoration(20, 2, false);
        rcvProduct.addItemDecoration(gridSpacingItemDecoration);

        AppCompatActivity activity = ((AppCompatActivity) requireActivity());
        Objects.requireNonNull(activity.getSupportActionBar()).setTitle(getResources().getString(R.string.home));

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("lkhai4617", "onViewCreated: HomeFragment");
        homeViewModel.refresh();
        loadUserInfo();
        loadBrands();
        loadProduct();
        filterProduct();
        observer();
        setupListener();

        productAdapter.setListener(new ProductAdapter.IOnClickProduct() {
            @Override
            public void onClickProduct(ProductModel productModel) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("product", productModel);

                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.action_homeFragment_to_productDetailFragment, bundle);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("lkhai4617", "onResume: HomeFragment");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("lkhai4617", "onDestroy: HomeFragment");
        binding = null;
    }

    private void loadUserInfo() {
        homeViewModel.getUser();
        homeViewModel.getUserLiveData().observe(getViewLifecycleOwner(), userModel -> {
            if (userModel != null) {
                binding.tvFullName.setText(userModel.getFullName());
                if (userModel.getAvatar() != null) {
                    Glide.with(requireContext())
                            .load(userModel.getAvatar())
                            .error(R.drawable.ic_user)
                            .into(binding.imgAvatar);
                }
            }
        });
    }

    private void loadProduct() {
        homeViewModel.loadNextPage();
        homeViewModel.getProductsLiveData().observe(getViewLifecycleOwner(), products -> {
            if (!products.isEmpty()) {
                productAdapter.updateProduct(products);
            }
            Log.d("lkhai4617", "products null");
        });

        homeViewModel.getIsLoadingLiveData().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading) {
                binding.progressBar.setVisibility(View.VISIBLE);
            } else {
                binding.progressBar.setVisibility(View.GONE);
            }
        });

        binding.nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY > oldScrollY) {
                    GridLayoutManager layoutManager = (GridLayoutManager) binding.rcvProduct.getLayoutManager();
                    if (layoutManager != null) {
                        int visibleItemCount = layoutManager.getChildCount();
                        int totalItemCount = layoutManager.getItemCount();
                        int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                        if (homeViewModel.getIsLoadingLiveData().getValue() != null && !homeViewModel.getIsLoadingLiveData().getValue() &&
                                (visibleItemCount + firstVisibleItemPosition) >= totalItemCount - 3) {
                            homeViewModel.loadNextPage();
                        }
                    }
                }
            }
        });
    }

    private void loadBrands() {
        homeViewModel.getBrands();
        homeViewModel.getBrandsLiveData().observe(getViewLifecycleOwner(), data -> {
            if (data != null) {
                for (BrandModel brand : data.getResult()) {
                    Chip chip = new Chip(binding.chipGroupBrands.getContext());
                    chip.setId(View.generateViewId());
                    chip.setText(brand.getName());
                    chip.setTag(brand.getId());
                    chip.setCheckable(true);
                    chip.setTextColor(getResources().getColorStateList(R.color.chip_text_color_change));
                    chip.setChipBackgroundColorResource(R.color.chip_bg_selected_change);
                    chip.setChipStrokeColorResource(android.R.color.black);
                    chip.setChipStrokeWidth(1);
                    chip.setChipCornerRadius(16);
                    chip.setCheckedIconEnabled(false);
                    binding.chipGroupBrands.addView(chip);
                }
            }
        });
    }

    private void filterProduct() {
        binding.chipGroupBrands.setOnCheckedStateChangeListener(new ChipGroup.OnCheckedStateChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull ChipGroup group, @NonNull List<Integer> checkedIds) {
                StringBuilder filter = new StringBuilder();
                if (!checkedIds.isEmpty()) {
                    filter.append("(");
                    List<String> brandsCondition = new ArrayList<>();
                    for (int checkedId : checkedIds) {
                        Chip chip = group.findViewById(checkedId);
                        if (chip != null && chip.getTag() != null) {
                            Integer brandId = (Integer) chip.getTag();
                            brandsCondition.add("brand: '" + brandId + "'");
                        }
                    }
                    filter.append(String.join(" or ", brandsCondition));
                    filter.append(")");
                }

                String query = binding.searchView.getQuery().toString();
                if (query != null && !query.isEmpty()) {
                    if (filter.length() > 0) {
                        filter.append(" and ");
                    }
                    filter.append("(name~'").append(query).append("')");
                }

                String newFilterString = filter.toString();
                String currentFilter = homeViewModel.getFilterLiveData().getValue();
                if (!newFilterString.equals(currentFilter)) {
                    if (newFilterString.isEmpty() && (currentFilter != null && !currentFilter.isEmpty())) {
                        homeViewModel.getFilterLiveData().setValue(null);
                    }
                    homeViewModel.getFilterLiveData().setValue(newFilterString);
                }
                Log.d("lkhai4617", "filterProduct: New filter = " + newFilterString);
            }
        });
    }

    private void observer() {
        homeViewModel.getFilterLiveData().observe(getViewLifecycleOwner(), str -> {
            String filterValue = (str != null) ? str : "";
            Log.d("lkhai4617", "observer: Filter changed to '" + filterValue + "', resetting and loading products");
            homeViewModel.resetAndLoad();
        });

        homeViewModel.getSortLiveData().observe(getViewLifecycleOwner(), str -> {
            String sortValue = (str != null) ? str : "";
            Log.d("lkhai4617", "observer: Sort changed to '" + sortValue + "', resetting and loading products");
            homeViewModel.resetAndLoad();
        });
    }

    private void setupListener() {
        binding.btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SortBottomSheetFragment fragment = SortBottomSheetFragment.newInstance(new SortBottomSheetFragment.IOnCLickItemSortDialog() {
                    @Override
                    public void onClickItemSortDialog(SortOption sortOption) {
                        binding.tvSelectedFilter.setText(sortOption.getName());
                        homeViewModel.getSortLiveData().setValue(sortOption.getRealData());
                    }
                });
                fragment.show(requireActivity().getSupportFragmentManager(), "sort_bottom_sheet_fragment");
            }
        });


        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                updateFilterWithSearchQuery(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        binding.btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.chipGroupBrands.clearCheck();
                binding.searchView.setQuery("", false);
                binding.searchView.clearFocus();
                binding.tvSelectedFilter.setText("Trống");
                homeViewModel.refresh();
            }
        });
    }

    private void updateFilterWithSearchQuery(String query) {

        StringBuilder filter = new StringBuilder();
        List<Integer> checkedBrandIds = binding.chipGroupBrands.getCheckedChipIds();
        if (!checkedBrandIds.isEmpty()) {
            filter.append("(");
            List<String> brandsCondition = new ArrayList<>();
            for (int checkedId : checkedBrandIds) {
                Chip chip = binding.chipGroupBrands.findViewById(checkedId);
                if (chip != null && chip.getTag() != null) {
                    Integer brandId = (Integer) chip.getTag();
                    brandsCondition.add("brand: '" + brandId + "'");
                }
            }
            filter.append(String.join(" or ", brandsCondition));
            filter.append(")");
        }

        if (query != null && !query.isEmpty()) {
            if (filter.length() > 0) {
                filter.append(" and ");
            }
            filter.append("(name~'").append(query).append("')");
        }

        String newFilterString = filter.toString();
        String currentFilter = homeViewModel.getFilterLiveData().getValue();
        if (!newFilterString.equals(currentFilter)) {
            if (newFilterString.isEmpty() && (currentFilter != null && !currentFilter.isEmpty())) {
                homeViewModel.getFilterLiveData().setValue(null);
            }
            homeViewModel.getFilterLiveData().setValue(newFilterString);
        }
        Log.d("lkhai4617", "updateFilterWithSearchQuery: New filter = " + newFilterString);
    }
}