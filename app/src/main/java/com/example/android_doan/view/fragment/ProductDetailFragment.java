package com.example.android_doan.view.fragment;

import static com.example.android_doan.view.fragment.ProductBottomSheetFragment.ACTION_KEY;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.android_doan.R;
import com.example.android_doan.adapter.ProductDetailViewPager2Adapter;
import com.example.android_doan.base.BaseViewModelFactory;
import com.example.android_doan.data.model.ProductModel;
import com.example.android_doan.data.model.request.AddToCartRequest;
import com.example.android_doan.data.model.response.GetCartResponse;
import com.example.android_doan.data.repository.RemoteRepository.ProductDetailRepository;
import com.example.android_doan.databinding.FragmentProductDetailBinding;
import com.example.android_doan.utils.FormatUtil;
import com.example.android_doan.viewmodel.ProductDetailViewModel;

import java.util.ArrayList;
import java.util.List;

public class ProductDetailFragment extends Fragment {
    public static final String ADD_TO_CART_ACTION = "ADD_TO_CART";
    public static final String BUY_NOW_ACTION = "BUY_NOW";
    private ProductModel productModel;
    private FragmentProductDetailBinding binding;
    private ProductDetailViewPager2Adapter viewPager2Adapter;
    private Boolean isExpanded = false;
    private RelativeLayout hiddenLayout;

    private ProductDetailViewModel productDetailViewModel;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            productModel = (ProductModel) bundle.getSerializable("product");
        }

        productDetailViewModel =
                new ViewModelProvider(
                        this,
                        new BaseViewModelFactory<ProductDetailRepository>(new ProductDetailRepository(), ProductDetailViewModel.class)
                ).get(ProductDetailViewModel.class);
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
        super.onViewCreated(view, savedInstanceState);
        setupViewPager2();
        setupUI();
        setupListener();
        observer();
    }

    private void setupViewPager2() {
        List<String> slider = productModel.getSlider();
        if (slider != null) {
            viewPager2Adapter = new ProductDetailViewPager2Adapter(slider);
            binding.viewPager2.setAdapter(viewPager2Adapter);
            binding.circleIndicator3.setViewPager(binding.viewPager2);
        }
    }

    private void setupListener() {
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
                ProductBottomSheetFragment dialogFragment = ProductBottomSheetFragment.newInstance(productModel, ADD_TO_CART_ACTION);
                dialogFragment.setListener(new ProductBottomSheetFragment.IOnClickAddToCart() {
                    @Override
                    public void onClickAddToCart(AddToCartRequest request) {
                        productDetailViewModel.addToCart(request);
                        dialogFragment.dismiss();
                    }

                    @Override
                    public void onClickBuyNow(GetCartResponse.Data product) {

                    }
                });

                dialogFragment.show(requireActivity().getSupportFragmentManager(), "product_dialog_fragment");
            }
        });

        binding.btnBuyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProductBottomSheetFragment dialogFragment = ProductBottomSheetFragment.newInstance(productModel, BUY_NOW_ACTION);
                dialogFragment.setListener(new ProductBottomSheetFragment.IOnClickAddToCart() {
                    @Override
                    public void onClickAddToCart(AddToCartRequest request) {
                        productDetailViewModel.addToCart(request);
                        dialogFragment.dismiss();
                    }

                    @Override
                    public void onClickBuyNow(GetCartResponse.Data product) {
//                        productDetailViewModel.getProduct(product.getProduct().getId());
                        GetCartResponse.Data data = new GetCartResponse.Data(product.getQuantity(), productModel);
                        ArrayList<GetCartResponse.Data> carts = new ArrayList<>();
                        carts.add(data);
                        if (carts.isEmpty()) {
                            return;
                        }
                        double total = product.getQuantity() * productModel.getPrice();
                        Bundle args = new Bundle();
                        args.putSerializable(CheckoutFragment.CHECKOUT_FRAGMENT_ITEM, carts);
                        args.putDouble(CheckoutFragment.CHECKOUT_FRAGMENT_TOTAL, total);
                        args.putString(ACTION_KEY, BUY_NOW_ACTION);
//                CheckoutFragment checkoutFragment = CheckoutFragment.newInstance(carts);
                        NavController navController = Navigation.findNavController(view);
                        navController.navigate(R.id.action_productDetailFragment_to_checkoutFragment, args);
                        dialogFragment.dismiss();
                    }
                });

                dialogFragment.show(requireActivity().getSupportFragmentManager(), "product_dialog_fragment");
            }
        });
    }

    private void setupUI() {
        if (productModel == null) {
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

    private void observer() {
        productDetailViewModel.getProductLiveData().observe(getViewLifecycleOwner(), productModel1 -> {
            if (productModel != null) {

            }
        });
    }

    private void showMoreDescription() {
        if (isExpanded) {
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