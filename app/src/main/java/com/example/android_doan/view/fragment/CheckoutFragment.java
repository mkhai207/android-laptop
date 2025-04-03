package com.example.android_doan.view.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.android_doan.R;
import com.example.android_doan.data.enums.OrderStatusEnum;
import com.example.android_doan.data.model.UserModel;
import com.example.android_doan.data.model.request.OrderRequest;
import com.example.android_doan.data.model.response.GetCartResponse;
import com.example.android_doan.data.repository.LocalRepository.DataLocalManager;
import com.example.android_doan.data.repository.RemoteRepository.CheckoutRepository;
import com.example.android_doan.databinding.FragmentCheckoutBinding;
import com.example.android_doan.utils.FormatUtil;
import com.example.android_doan.viewmodel.CheckoutViewModel;
import com.example.android_doan.viewmodel.CheckoutViewModelFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CheckoutFragment extends Fragment {
    private FragmentCheckoutBinding binding;
    private List<GetCartResponse.Data> mCarts;
    private double mTotal;
    public static final String CHECKOUT_FRAGMENT_ITEM = "com.example.android_doan.view.fragment.CHECKOUT_FRAGMENT_ITEM";
    public static final String CHECKOUT_FRAGMENT_TOTAL = "com.example.android_doan.view.fragment.CHECKOUT_FRAGMENT_TOTAL";
    private CheckoutViewModel checkoutViewModel;
    private CheckoutRepository repository;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null){
            mTotal = args.getDouble(CHECKOUT_FRAGMENT_TOTAL);
            Serializable serializable = args.getSerializable(CHECKOUT_FRAGMENT_ITEM);
            if (serializable instanceof List) {
                try {
                    mCarts = (List<GetCartResponse.Data>) serializable;
                } catch (ClassCastException e) {
                    e.printStackTrace();
                    mCarts = new ArrayList<>();
                    Toast.makeText(requireContext(), "Lỗi khi lấy dữ liệu giỏ hàng", Toast.LENGTH_SHORT).show();
                }
            } else {
                mCarts = new ArrayList<>();
                Toast.makeText(requireContext(), "Dữ liệu giỏ hàng không hợp lệ", Toast.LENGTH_SHORT).show();
            }
        }
        repository = new CheckoutRepository();
        checkoutViewModel = new ViewModelProvider(requireActivity(), new CheckoutViewModelFactory(repository)).get(CheckoutViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCheckoutBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupData();
        setupListener();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    private void setupData(){
        binding.tvTotalData.setText(FormatUtil.formatCurrency(mTotal));
        checkoutViewModel.getAddress().observe(getViewLifecycleOwner(), address -> {
            if (address != null) {
                Log.d("lkhai4617", "setupData: CheckoutFragment");
                binding.tvReceiverName.setText(address.getName());
                binding.tvReceiverPhone.setText(address.getPhone());
                binding.tvAddressDetail.setText(address.getAddress());
            }
        });
    }

    private void setupListener(){
        binding.tvChangeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EdtAddressBottomSheetFragment edtAddressDialogFragment = new EdtAddressBottomSheetFragment();
                edtAddressDialogFragment.show(requireActivity().getSupportFragmentManager(), "edt_address_dialog_fragment");
            }
        });

        binding.btnPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int checkedId = binding.rbtnGroup.getCheckedRadioButtonId();
                if (checkedId == -1){
                    Toast.makeText(requireContext(), "Vui lòng chọn phương thức thanh toán", Toast.LENGTH_SHORT).show();
                    return;
                }
                String paymentMethod = "";
                switch (checkedId){
                    case R.id.rbtn_online:
                        paymentMethod = "ONLINE";
                        break;
                    case R.id.rbtn_cash:
                        paymentMethod = "CASH";
                        break;
                }

                double totalMoney = FormatUtil.parseCurrency(binding.tvTotalData.getText().toString());
                String amountPaid = "0";
                OrderStatusEnum status = OrderStatusEnum.PENDING;
                String shippingAddress = binding.tvAddressDetail.getText().toString();
                String name = binding.tvReceiverName.getText().toString();
                String phone = binding.tvReceiverPhone.getText().toString();
                UserModel user = new UserModel(Integer.parseInt(DataLocalManager.getUserId()));

                List<OrderRequest.OrderDetail> orderDetails = new ArrayList<>();
                for (GetCartResponse.Data item : mCarts){
                    double price = (long) item.getQuantity() * item.getProduct().getPrice();
                    orderDetails.add(new OrderRequest.OrderDetail(price, item.getQuantity(), item.getProduct().getId()));
                }
                OrderRequest request = new OrderRequest(totalMoney, paymentMethod, amountPaid, status, shippingAddress, name, phone, user, orderDetails);
                checkoutViewModel.placeOrder(request, isSuccess -> {
                    if (isSuccess){
                        checkoutViewModel.clearCart(isSuccess1 -> {
                            if (!isSuccess1){
                                Toast.makeText(requireContext(), "Clear cart failure", Toast.LENGTH_SHORT).show();
                            }
                        });

                        NavController navController = Navigation.findNavController(view);
                        navController.navigate(R.id.action_checkoutFragment_to_orderSuccessFragment);
                    }
                });
            }
        });
    }
}