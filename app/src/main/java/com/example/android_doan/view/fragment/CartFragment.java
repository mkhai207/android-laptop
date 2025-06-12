package com.example.android_doan.view.fragment;

import static com.example.android_doan.view.fragment.ProductBottomSheetFragment.ACTION_KEY;
import static com.example.android_doan.view.fragment.ProductDetailFragment.ADD_TO_CART_ACTION;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android_doan.R;
import com.example.android_doan.adapter.CartAdapter;
import com.example.android_doan.base.BaseViewModelFactory;
import com.example.android_doan.data.model.request.AddToCartRequest;
import com.example.android_doan.data.model.response.GetCartResponse;
import com.example.android_doan.data.repository.RemoteRepository.CartRepository;
import com.example.android_doan.databinding.FragmentCartBinding;
import com.example.android_doan.utils.FormatUtil;
import com.example.android_doan.viewmodel.CartViewModel;

import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment {
    private FragmentCartBinding binding;
    private CartViewModel cartViewModel;
    private RecyclerView rcvItemCart;
    private CartAdapter cartAdapter;
    private List<GetCartResponse.Data> mCarts;
    private double mTotal;

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
        cartViewModel.getTotalPriceCart().observe(getViewLifecycleOwner(), total -> {
            if (total != null) {
                mTotal = total;
                String totalPrice = FormatUtil.formatCurrency(mTotal);
                binding.tvTotalData.setText(totalPrice);
            }
        });

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
            if (resource != null) {
                switch (resource.getStatus()) {
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

        setupListener();

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    private void setupRecyclerView() {
        rcvItemCart = binding.rcvItemCart;
        cartAdapter = new CartAdapter(new ArrayList<>());
        rcvItemCart.setAdapter(cartAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        rcvItemCart.setLayoutManager(linearLayoutManager);
    }

    private void setupViewModel() {
        cartViewModel = new ViewModelProvider(
                requireActivity(),
                new BaseViewModelFactory<CartRepository>(new CartRepository(), CartViewModel.class)
        ).get(CartViewModel.class);
    }

    private void loadItemCart() {
        cartViewModel.getCart();
        cartViewModel.getItemCartLiveData().observe(getViewLifecycleOwner(), itemCarts -> {
            if (itemCarts != null) {
                mCarts = itemCarts;
                cartAdapter.updateItemCart(itemCarts);
            }
        });
    }

    private void setupListener() {
        binding.btnPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<GetCartResponse.Data> carts = new ArrayList<>(mCarts);
                if (carts.isEmpty()) {
                    return;
                }

                Bundle args = new Bundle();
                args.putSerializable(CheckoutFragment.CHECKOUT_FRAGMENT_ITEM, carts);
                args.putDouble(CheckoutFragment.CHECKOUT_FRAGMENT_TOTAL, mTotal);
                args.putString(ACTION_KEY, ADD_TO_CART_ACTION);
//                CheckoutFragment checkoutFragment = CheckoutFragment.newInstance(carts);
                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.action_cartFragment_to_checkoutFragment, args);
            }
        });
    }
}