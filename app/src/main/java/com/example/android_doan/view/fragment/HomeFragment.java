package com.example.android_doan.view.fragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.android_doan.R;
import com.example.android_doan.adapter.ProductAdapter;
import com.example.android_doan.data.model.ProductModel;
import com.example.android_doan.data.repository.LocalRepository.DataLocalManager;
import com.example.android_doan.data.repository.RemoteRepository.HomeRepository;
import com.example.android_doan.databinding.FragmentHomeBinding;
import com.example.android_doan.utils.GridSpacingItemDecoration;
import com.example.android_doan.viewmodel.HomeViewModel;
import com.example.android_doan.viewmodel.HomeViewModelFactory;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private HomeViewModel homeViewModel;
    private HomeRepository homeRepository;
    private ProductAdapter productAdapter;
    private RecyclerView rcvProduct;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        homeRepository = new HomeRepository();
        HomeViewModelFactory homeViewModelFactory = new HomeViewModelFactory(homeRepository);
        homeViewModel = new ViewModelProvider(this, homeViewModelFactory).get(HomeViewModel.class);

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        rcvProduct = binding.rcvProduct;
        productAdapter = new ProductAdapter(requireContext(),new ArrayList<>());
        rcvProduct.setAdapter(productAdapter);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 2);
        rcvProduct.setLayoutManager(gridLayoutManager);

        GridSpacingItemDecoration gridSpacingItemDecoration = new GridSpacingItemDecoration(20, 2,false);
        rcvProduct.addItemDecoration(gridSpacingItemDecoration);

        AppCompatActivity activity = ((AppCompatActivity)requireActivity());
        activity.getSupportActionBar().setTitle(getResources().getString(R.string.home));

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d("access_token", DataLocalManager.getAccessToken());
        loadUserInfo();
        loadProduct();

        productAdapter.setListener(new ProductAdapter.IOnClickProduct() {
            @Override
            public void onClickProduct(ProductModel productModel) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("product", productModel);

                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.action_homeFragment_to_productDetailFragment, bundle);
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroy() {
        binding = null;
        super.onDestroy();
    }

    private void loadUserInfo(){
        homeViewModel.getUser();
        homeViewModel.getUserLiveData().observe(getViewLifecycleOwner(), userModel -> {
            if (userModel != null){
                binding.tvFullName.setText(userModel.getFullName());
                if (userModel.getAvatar() != null){
                    Glide.with(requireContext())
                            .load("http://192.168.50.2:8080/" + userModel.getAvatar())
                            .error(R.drawable.ic_user)
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    Log.e("lkhai4617", "Glide load failed: " + e.getMessage());
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    Log.d("lkhai4617", "Glide load success");
                                    return false;
                                }
                            })
                            .into(binding.imgAvatar);
                }
            }
        });
    }

    private void loadProduct(){
        homeViewModel.loadNextPage();
        homeViewModel.getProductsLiveData().observe(getViewLifecycleOwner(), products -> {
            if (products != null){
                Log.d("lkhai4617", products.get(0).getName());
                productAdapter.updateProduct(products);
            }
            Log.d("lkhai4617", "products null");
        });

        homeViewModel.getIsLoadingLiveData().observe(getViewLifecycleOwner(), isLoading ->{
            if (isLoading){
                binding.progressBar.setVisibility(View.VISIBLE);
            }else {
                binding.progressBar.setVisibility(View.GONE);
            }
        });

//        rcvProduct.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//            }
//
//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                if (dy > 0){
//                    GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
//                    if (layoutManager != null){
//                        int visibleItemCount = layoutManager.getChildCount();
//                        int totalItemCount = layoutManager.getItemCount();
//                        int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
//
//                        if (homeViewModel.getIsLoadingLiveData().getValue() != null && !homeViewModel.getIsLoadingLiveData().getValue() &&
//                                (visibleItemCount + firstVisibleItemPosition) >= totalItemCount - 5) {
//                            homeViewModel.loadNextPage();
//                        }
//                    }
//                }
//            }
//        });

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
                                (visibleItemCount + firstVisibleItemPosition) >= totalItemCount - 5) {
                            homeViewModel.loadNextPage();
                        }
                    }
                }
            }
        });

    }
}