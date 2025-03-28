package com.example.android_doan.view.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.android_doan.R;
import com.example.android_doan.adapter.CartAdapter;
import com.example.android_doan.data.model.request.AddToCartRequest;
import com.example.android_doan.data.repository.RemoteRepository.CartRepository;
import com.example.android_doan.databinding.FragmentCartBinding;
import com.example.android_doan.viewmodel.CartViewModeFactory;
import com.example.android_doan.viewmodel.CartViewModel;

import java.util.ArrayList;

public class CartFragment extends Fragment {
    private FragmentCartBinding binding;
    private CartViewModel cartViewModel;
    private RecyclerView rcvItemCart;
    private CartAdapter cartAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCartBinding.inflate(inflater, container, false);

        setupRecyclerView();
        setupViewModel();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        loadItemCart();
        cartAdapter.setListener(new CartAdapter.IOnClickItemCart() {
            @Override
            public void onClickAdjustQuantity(AddToCartRequest request) {
                cartViewModel.updateQuantity(request);
            }

            @Override
            public void onClickRemove(String productId) {
                cartViewModel.removeCart(productId);
            }

        });

        cartViewModel.getActionResult().observe(getViewLifecycleOwner(), resource -> {
            if (resource != null){
                switch (resource.getStatus()){
                    case LOADING:
                        binding.progressBar.setVisibility(View.VISIBLE);
                        break;
                    case SUCCESS:
                        binding.progressBar.setVisibility(View.GONE);
                        break;
                    case ERROR:
                        Toast.makeText(requireContext(), resource.getMessage(), Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    private void setupRecyclerView(){
        rcvItemCart = binding.rcvItemCart;
        cartAdapter = new CartAdapter(new ArrayList<>());
        rcvItemCart.setAdapter(cartAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        rcvItemCart.setLayoutManager(linearLayoutManager);
    }

    private void setupViewModel(){
        CartRepository repository = new CartRepository();
        CartViewModeFactory cartViewModeFactory = new CartViewModeFactory(repository);
        cartViewModel = new ViewModelProvider(this, cartViewModeFactory).get(CartViewModel.class);
    }

    private void loadItemCart(){
        cartViewModel.getCart();
        cartViewModel.getItemCartLiveData().observe(getViewLifecycleOwner(), itemCarts ->{
            if (itemCarts != null){
                Log.d("lkhai4617", "updateItemCart: success");
                cartAdapter.updateItemCart(itemCarts);
            } else {
                Log.d("lkhai4617", "loadItemCart: null");
            }
        });
    }
}