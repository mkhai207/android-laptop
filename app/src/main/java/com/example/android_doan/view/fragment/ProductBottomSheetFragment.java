package com.example.android_doan.view.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.android_doan.R;
import com.example.android_doan.data.model.request.AddToCartRequest;
import com.example.android_doan.data.model.ProductModel;
import com.example.android_doan.databinding.ItemProductDialogFragmentBinding;
import com.example.android_doan.utils.FormatUtil;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class ProductBottomSheetFragment extends BottomSheetDialogFragment {

    private static final String PRODUCT_MODEL = "com.example.android_doan.view.fragment.KEY";
    ItemProductDialogFragmentBinding binding;
    ProductModel productModel;

    public interface IOnClickAddToCart{
        void onClickAddToCart(AddToCartRequest request);
    }

    IOnClickAddToCart listener;
    public void setListener(IOnClickAddToCart listener){
        this.listener = listener;
    }

    public static ProductBottomSheetFragment newInstance(ProductModel productModel) {

        Bundle args = new Bundle();
        args.putSerializable(PRODUCT_MODEL, productModel);

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
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        binding = ItemProductDialogFragmentBinding.inflate(getLayoutInflater());
        bottomSheetDialog.setContentView(binding.getRoot());

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
                AddToCartRequest request = new AddToCartRequest(String.valueOf(quantity), id);
                listener.onClickAddToCart(request);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
