package com.example.android_doan.view.fragment;

import static com.example.android_doan.view.fragment.AddOrUpdateAddressFragment.ADDRESS_KEY;
import static com.example.android_doan.view.fragment.AddressFragment.REQUEST_KEY_ADDRESS;
import static com.example.android_doan.view.fragment.ProductBottomSheetFragment.ACTION_KEY;
import static com.example.android_doan.view.fragment.ProductDetailFragment.ADD_TO_CART_ACTION;

import android.os.Bundle;
import android.util.Log;
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
import androidx.navigation.fragment.NavHostFragment;

import com.example.android_doan.R;
import com.example.android_doan.base.BaseViewModelFactory;
import com.example.android_doan.data.enums.OrderStatusEnum;
import com.example.android_doan.data.model.UserModel;
import com.example.android_doan.data.model.request.OrderRequest;
import com.example.android_doan.data.model.response.AddressResponse;
import com.example.android_doan.data.model.response.GetCartResponse;
import com.example.android_doan.data.repository.LocalRepository.DataLocalManager;
import com.example.android_doan.data.repository.RemoteRepository.CheckoutRepository;
import com.example.android_doan.databinding.FragmentCheckoutBinding;
import com.example.android_doan.utils.CustomToast;
import com.example.android_doan.utils.FormatUtil;
import com.example.android_doan.viewmodel.CheckoutViewModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CheckoutFragment extends Fragment {
    public static final String CHECKOUT_FRAGMENT_ITEM = "com.example.android_doan.view.fragment.CHECKOUT_FRAGMENT_ITEM";
    public static final String CHECKOUT_FRAGMENT_TOTAL = "com.example.android_doan.view.fragment.CHECKOUT_FRAGMENT_TOTAL";
    private FragmentCheckoutBinding binding;
    private List<GetCartResponse.Data> mCarts;
    private double mTotal;
    private String mAction;
    private CheckoutViewModel checkoutViewModel;
    private CheckoutRepository repository;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mTotal = args.getDouble(CHECKOUT_FRAGMENT_TOTAL);
            mAction = args.getString(ACTION_KEY);
            Serializable serializable = args.getSerializable(CHECKOUT_FRAGMENT_ITEM);
            if (serializable instanceof List) {
                try {
                    mCarts = (List<GetCartResponse.Data>) serializable;
                } catch (ClassCastException e) {
                    e.printStackTrace();
                    mCarts = new ArrayList<>();
                    Log.d("lkhai4617", "Lỗi khi lấy dữ liệu giỏ hàng");
                }
            } else {
                mCarts = new ArrayList<>();
                Log.d("lkhai4617", "Dữ liệu giỏ hàng không hợp lệ");
            }
        }

        checkoutViewModel = new ViewModelProvider(
                requireActivity(),
                new BaseViewModelFactory<CheckoutRepository>(new CheckoutRepository(), CheckoutViewModel.class)
        ).get(CheckoutViewModel.class);

        String userId = DataLocalManager.getUserId();
        checkoutViewModel.getAddressDefault("user:'" + userId + "' and isDefault: " + true);
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
        setupFragmentResultListener();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    private void setupData() {
        binding.tvTotalData.setText(FormatUtil.formatCurrency(mTotal));
        checkoutViewModel.getAddressLiveData().observe(getViewLifecycleOwner(), addressResponses -> {
            if (addressResponses != null && !addressResponses.isEmpty()) {
                AddressResponse addressResponse = addressResponses.get(0);
                binding.tvFullName.setText(addressResponse.getRecipientName());
                binding.tvNumPhone.setText(addressResponse.getPhoneNumber());
                String address = addressResponse.getStreet() + ", " + addressResponse.getWard() + ", " + addressResponse.getDistrict() + ", " + addressResponse.getCity();
                binding.tvAddress.setText(address);
            }
        });
    }

    private void setupListener() {
        binding.tvChangeAddress.setOnClickListener(view -> openAddressFragment());

        binding.layoutAddress.setOnClickListener(view -> openAddressFragment());

        binding.btnPayment.setOnClickListener(view -> {
            int checkedId = binding.rbtnGroup.getCheckedRadioButtonId();
            if (checkedId == -1) {
                CustomToast.showToast(requireContext(), "Vui lòng chọn phương thức thanh toán", Toast.LENGTH_SHORT);
                return;
            }
            String paymentMethod = "";
            switch (checkedId) {
                case R.id.rbtn_online:
                    paymentMethod = "ONLINE";
                    break;
                case R.id.rbtn_cash:
                    paymentMethod = "CASH";
                    break;
            }

//                double totalMoney = FormatUtil.parseCurrency(binding.tvTotalData.getText().toString());
//                String amountPaid = "0";
            OrderStatusEnum status = OrderStatusEnum.PENDING;
//                String shippingAddress = binding.tvAddress.getText().toString();
//                String name = binding.tvFullName.getText().toString();
//                String phone = binding.tvNumPhone.getText().toString();
            UserModel user = new UserModel(Integer.parseInt(DataLocalManager.getUserId()));

            List<OrderRequest.OrderDetail> orderDetails = new ArrayList<>();
            for (GetCartResponse.Data item : mCarts) {
//                double price = item.getQuantity() * item.getProduct().getPrice();
                orderDetails.add(new OrderRequest.OrderDetail(item.getQuantity(), item.getProduct().getId()));
            }

            // check address is empty
            if (checkoutViewModel.getAddressLiveData().getValue() == null || checkoutViewModel.getAddressLiveData().getValue().isEmpty()) {
                CustomToast.showToast(requireContext(), "Vui lòng chọn địa chỉ giao hàng!", Toast.LENGTH_LONG);
                return;
            }
            AddressResponse address = checkoutViewModel.getAddressLiveData().getValue().get(0);

            // create order
            OrderRequest request = new OrderRequest(status, paymentMethod, user, orderDetails, address);
            checkoutViewModel.placeOrder(request, isSuccess -> {
                if (isSuccess && Objects.equals(mAction, ADD_TO_CART_ACTION)) {
                    checkoutViewModel.clearCart(isSuccess1 -> {
                        if (!isSuccess1) {
                            Toast.makeText(requireContext(), "Xóa giỏ hàng thất bại!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.action_checkoutFragment_to_orderSuccessFragment);
            });
        });
    }

    private void openAddressFragment() {
        NavHostFragment navHostFragment =
                (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.frag_container);
        if (navHostFragment != null) {
            NavController navController = navHostFragment.getNavController();
            navController.navigate(R.id.action_checkoutFragment_to_addressFragment);
        }
    }

    private void setupFragmentResultListener() {
        getParentFragmentManager().setFragmentResultListener(REQUEST_KEY_ADDRESS, getViewLifecycleOwner(), (requestKey, result) -> {
            AddressResponse addressResponse = (AddressResponse) result.getSerializable(ADDRESS_KEY);
            if (addressResponse != null) {
                List<AddressResponse> address = new ArrayList<>();
                address.add(addressResponse);
                checkoutViewModel.getAddressLiveData().setValue(address);
            }
        });
    }
}