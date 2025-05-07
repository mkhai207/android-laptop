package com.example.android_doan.view.fragment;

import static com.example.android_doan.view.fragment.ProductDetailFragment.ADD_TO_CART_ACTION;
import static com.example.android_doan.view.fragment.ProductDetailFragment.BUY_NOW_ACTION;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.android_doan.R;
import com.example.android_doan.data.model.request.AddToCartRequest;
import com.example.android_doan.data.model.ProductModel;
import com.example.android_doan.data.model.response.GetCartResponse;
import com.example.android_doan.databinding.ItemProductDialogFragmentBinding;
import com.example.android_doan.utils.FormatUtil;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Objects;

public class ProductBottomSheetFragment extends BottomSheetDialogFragment {

    private static final String PRODUCT_MODEL = "com.example.android_doan.view.fragment.PRODUCT_MODEL";
    public static final String ACTION_KEY = "com.example.android_doan.view.fragment.ACTION_KEY";
    private ItemProductDialogFragmentBinding binding;
    private ProductModel productModel;
    private String mAction;

    public interface IOnClickAddToCart{
        void onClickAddToCart(AddToCartRequest request);
        void onClickBuyNow(GetCartResponse.Data product);
    }

    IOnClickAddToCart listener;
    public void setListener(IOnClickAddToCart listener){
        this.listener = listener;
    }

    public static ProductBottomSheetFragment newInstance(ProductModel productModel, String action) {

        Bundle args = new Bundle();
        args.putSerializable(PRODUCT_MODEL, productModel);
        args.putString(ACTION_KEY, action);

        ProductBottomSheetFragment fragment = new ProductBottomSheetFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null){
            productModel = (ProductModel) bundle.getSerializable(PRODUCT_MODEL);
            mAction = bundle.getString(ACTION_KEY);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        binding = ItemProductDialogFragmentBinding.inflate(getLayoutInflater());
        bottomSheetDialog.setContentView(binding.getRoot());

        if (Objects.equals(mAction, BUY_NOW_ACTION)){
            binding.btnAddToCart.setText(getResources().getString(R.string.buy_now));
        }

        setData();
        setupListener();

        return bottomSheetDialog;
    }
    private void setData(){
        if (productModel == null){
            return;
        }

        Glide.with(requireActivity())
                .load(productModel.getThumbnail())
                .error(R.drawable.ic_user)
                .into(binding.ivLaptop);

        binding.tvNameOfLaptop.setText(productModel.getName());
        String price = FormatUtil.formatCurrency(productModel.getPrice());
        binding.tvPriceOfLaptop.setText(price);
    }

    private void setupListener(){
        binding.ivIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quantity = Integer.parseInt(binding.tvQuantity.getText().toString());
                if (quantity < productModel.getQuantity()){
                    binding.tvQuantity.setText(String.valueOf(quantity + 1));
                }
            }
        });

        binding.ivDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quantity = Integer.parseInt(binding.tvQuantity.getText().toString());
                if (quantity > 1){
                    binding.tvQuantity.setText(String.valueOf(quantity - 1));
                }
            }
        });

        binding.btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quantity = Integer.parseInt(binding.tvQuantity.getText().toString());
                String id = productModel.getId();
                if (Objects.equals(mAction, ADD_TO_CART_ACTION)){
                    AddToCartRequest request = new AddToCartRequest(String.valueOf(quantity), id);
                    listener.onClickAddToCart(request);
                } else {
                    GetCartResponse.Data data = new GetCartResponse.Data(quantity, new ProductModel(id));
                    listener.onClickBuyNow(data);
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
