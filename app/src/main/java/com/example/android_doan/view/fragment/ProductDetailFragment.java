package com.example.android_doan.view.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.android_doan.R;
import com.example.android_doan.adapter.ProductDetailViewPager2Adapter;
import com.example.android_doan.data.model.request.AddToCartRequest;
import com.example.android_doan.data.model.ProductModel;
import com.example.android_doan.databinding.FragmentProductDetailBinding;
import com.example.android_doan.utils.FormatUtil;
import com.example.android_doan.viewmodel.ProductDetailViewModel;

import java.util.List;

public class ProductDetailFragment extends Fragment {
    ProductModel productModel;
    FragmentProductDetailBinding binding;
    ProductDetailViewPager2Adapter viewPager2Adapter;
    Boolean isExpanded = false;
    RelativeLayout hiddenLayout;

    ProductDetailViewModel productDetailViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        if (bundle != null){
            productModel = (ProductModel) bundle.getSerializable("product");
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProductDetailBinding.inflate(inflater, container, false);
        hiddenLayout = binding.hiddenLayout;

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        setupViewPager2();
        setupUI();
        setupListener();
        productDetailViewModel = new ViewModelProvider(this).get(ProductDetailViewModel.class);

        super.onViewCreated(view, savedInstanceState);
    }
    private void setupViewPager2(){
        List<String> slider = productModel.getSlider();
        if (slider != null){
            viewPager2Adapter = new ProductDetailViewPager2Adapter(slider);
            binding.viewPager2.setAdapter(viewPager2Adapter);
            binding.circleIndicator3.setViewPager(binding.viewPager2);
        }
    }

    private void setupListener(){
        binding.btnSeeMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMoreDescription();
            }
        });

//        binding.btnBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                requireActivity().getSupportFragmentManager().popBackStack();
//            }
//        });

        binding.btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProductDialogFragment dialogFragment = ProductDialogFragment.newInstance(productModel);
                dialogFragment.setListener(new ProductDialogFragment.IOnClickAddToCart() {
                    @Override
                    public void onClickAddToCart(AddToCartRequest request) {
                        productDetailViewModel.addToCart(request, requireContext());
                        dialogFragment.dismiss();
                    }
                });

                dialogFragment.show(requireActivity().getSupportFragmentManager(), "product_dialog_fragment");
            }
        });
    }

    private void setupUI(){
        if (productModel == null){
            return;
        }
        binding.tvDescriptionContent.setText(productModel.getDescription());
        binding.tvNameOfLaptop.setText(productModel.getName());
        String price = FormatUtil.formatCurrency(productModel.getPrice());
        binding.tvPriceOfLaptop.setText(price);
        binding.tvColorInfo.setText(productModel.getColor());
        binding.tvCpuInfo.setText(productModel.getCpu());
        binding.tvGpuInfo.setText(productModel.getGpu());
        binding.tvRamInfo.setText(String.valueOf(productModel.getRam()));
        String rom = productModel.getMemoryType() + " " + productModel.getMemory();
        binding.tvRomInfo.setText(rom);
        String other = productModel.getOs() + ", " + productModel.getPort();
        binding.tvOtherInfo.setText(other);
    }

    private void showMoreDescription(){
        if (isExpanded){
            binding.tvDescriptionContent.setMaxLines(3);
            hiddenLayout.setVisibility(View.GONE);
            binding.btnSeeMore.setText(R.string.see_more);
            isExpanded = false;
        } else {
            binding.tvDescriptionContent.setMaxLines(Integer.MAX_VALUE);
            hiddenLayout.setVisibility(View.VISIBLE);
            binding.btnSeeMore.setText(R.string.collapse);
            isExpanded = true;
        }
    }
}