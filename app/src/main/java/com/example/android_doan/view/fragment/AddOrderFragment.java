package com.example.android_doan.view.fragment;

import static androidx.navigation.fragment.FragmentKt.findNavController;
import static com.example.android_doan.view.fragment.ProductManagementFragment.PRODUCT_KEY;
import static com.example.android_doan.view.fragment.ProductManagementFragment.REQUEST_KEY_PRODUCT;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.android_doan.R;
import com.example.android_doan.adapter.OrderItemAdapter;
import com.example.android_doan.base.BaseViewModelFactory;
import com.example.android_doan.data.model.OrderItem;
import com.example.android_doan.data.model.ProductModel;
import com.example.android_doan.data.model.UserModel;
import com.example.android_doan.data.model.request.CreateAdminOrderRequest;
import com.example.android_doan.data.repository.RemoteRepository.OrderManagementRepository;
import com.example.android_doan.databinding.FragmentAddOrderBinding;
import com.example.android_doan.utils.CustomToast;
import com.example.android_doan.viewmodel.OrderManagementViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AddOrderFragment extends Fragment {
    private FragmentAddOrderBinding binding;
    private ArrayAdapter<UserModel> userAdapter;
    private List<UserModel> mListUser = new ArrayList<>();
    private UserModel selectedUser;
    private List<OrderItem> mListProduct = new ArrayList<>();
    private OrderItemAdapter orderItemAdapter;
    private OrderManagementViewModel orderManagementViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (selectedUser != null) {
            outState.putSerializable("SELECTED_USER_KEY", selectedUser);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        orderManagementViewModel =
                new ViewModelProvider(
                        this,
                        new BaseViewModelFactory<OrderManagementRepository>(new OrderManagementRepository(), OrderManagementViewModel.class)
                ).get(OrderManagementViewModel.class);

        binding = FragmentAddOrderBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState != null) {
            selectedUser = (UserModel) savedInstanceState.getSerializable("SELECTED_USER_KEY");
        }
        observer();
        handleStatus();
        initUI();
        setupListener();
        setupFragmentResultListener();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    private void observer() {
        orderManagementViewModel.getUsersLiveData().observe(getViewLifecycleOwner(), users -> {
            if (users != null && !users.isEmpty()) {
                mListUser = users;
                setupSpinner();

                if (selectedUser != null) {
                    for (int i = 0; i < mListUser.size(); i++) {
                        if (mListUser.get(i).getId() == selectedUser.getId()) {
                            selectedUser = mListUser.get(i);
                            binding.spinnerUsers.setSelection(i);
                            break;
                        }
                    }
                }
            }
        });
        orderManagementViewModel.getAllUser();
    }

    private void handleStatus() {
        orderManagementViewModel.getApiResultLiveData().observe(getViewLifecycleOwner(), apiResult -> {
            if (apiResult != null) {
                switch (apiResult.getStatus()) {
                    case LOADING:
                        binding.progressBar.setVisibility(View.VISIBLE);
                        break;
                    case SUCCESS:
                        binding.progressBar.setVisibility(View.GONE);
                        switch (apiResult.getMessage()) {
                            case "createOrder":
                                CustomToast.showToast(requireContext(), "Thành công", Toast.LENGTH_SHORT);
                                findNavController(this).popBackStack();
                                break;
                        }
                        break;
                    case ERROR:
                        binding.progressBar.setVisibility(View.GONE);
                        CustomToast.showToast(requireContext(), apiResult.getMessage(), 2000);
                        break;
                }
            }
        });
    }

    private void initUI() {
        setupRecyclerView();
    }

    private void setupListener() {
        binding.btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openProductManagement();
            }
        });

        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createOrder();
            }
        });

        binding.spinnerUsers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                UserModel userModel = (UserModel) adapterView.getSelectedItem();
                selectedUser = new UserModel(userModel.getId());
                Log.d("lkhai4617", "onItemSelected: " + selectedUser.getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setupSpinner() {
        userAdapter = new ArrayAdapter<UserModel>(
                requireContext(), android.R.layout.simple_spinner_item, mListUser) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                TextView textView = (TextView) super.getView(position, convertView, parent);
                if (getItem(position) != null) {
                    textView.setText(getItem(position).getFullName() + " - " + getItem(position).getEmail());
                }
                return textView;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                TextView textView = (TextView) super.getDropDownView(position, convertView, parent);
                if (getItem(position) != null) {
                    textView.setText(getItem(position).getFullName() + " - " + getItem(position).getEmail());
                }
                return textView;
            }
        };
        userAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerUsers.setAdapter(userAdapter);
    }

    private void openProductManagement() {
        Bundle bundle = new Bundle();
        bundle.putBoolean("ACTION_BACK_KEY", true);

        NavController navController = NavHostFragment.findNavController(this);
        navController.navigate(R.id.action_addOrderFragment_to_productManagementFragment, bundle);
    }

    private void setupFragmentResultListener() {
        getParentFragmentManager().setFragmentResultListener(REQUEST_KEY_PRODUCT, getViewLifecycleOwner(), (requestKey, result) -> {
            ProductModel productModel = (ProductModel) result.getSerializable(PRODUCT_KEY);
            if (productModel != null) {
                OrderItem orderItem = new OrderItem(productModel.getId(), productModel.getPrice(), 1, productModel.getName(), productModel.getThumbnail());
                boolean isExist = false;
                for (OrderItem item : mListProduct) {
                    if (Objects.equals(item.getId(), orderItem.getId())) {
                        item.setQuantity(item.getQuantity() + 1);
                        isExist = true;
                        break;
                    }
                }
                if (!isExist) {
                    mListProduct.add(orderItem);
                }
                orderItemAdapter.updateData(mListProduct);
            }
        });
    }

    private void setupRecyclerView() {
        orderItemAdapter = new OrderItemAdapter(new ArrayList<>());
        binding.rcvOrderItems.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rcvOrderItems.setAdapter(orderItemAdapter);
        orderItemAdapter.updateData(mListProduct);

        orderItemAdapter.setListener(new OrderItemAdapter.IOnClickOrderItem() {
            @Override
            public void onClickIncreaseQuantity(OrderItem orderItem) {
                for (OrderItem item : mListProduct) {
                    if (Objects.equals(item.getId(), orderItem.getId())) {
                        item.setQuantity(item.getQuantity() + 1);
                        orderItemAdapter.notifyDataSetChanged();
                        break;
                    }
                }
            }

            @Override
            public void onClickDecreaseQuantity(OrderItem orderItem) {
                for (OrderItem item : mListProduct) {
                    if (Objects.equals(item.getId(), orderItem.getId())) {
                        item.setQuantity(item.getQuantity() - 1);
                        orderItemAdapter.notifyDataSetChanged();
                        break;
                    }
                }
            }
        });
    }

    private void createOrder() {
        if (mListProduct == null || mListProduct.isEmpty()) {
            CustomToast.showToast(requireContext(), "Vui lòng chọn thêm sản phẩm!", Toast.LENGTH_SHORT);
            return;
        }

        if (binding.edtShippingAddress.getText().toString().trim().isBlank()) {
            CustomToast.showToast(requireContext(), "Vui lòng nhập địa chỉ nhận hàng!", Toast.LENGTH_SHORT);
            return;
        }

        if (binding.edtRecipientName.getText().toString().trim().isBlank()) {
            CustomToast.showToast(requireContext(), "Vui lòng nhập tên người nhận hàng!", Toast.LENGTH_SHORT);
            return;
        }

        if (binding.edtPhoneNumber.getText().toString().trim().isBlank()) {
            CustomToast.showToast(requireContext(), "Vui lòng nhập số điện thoại nhận hàng!", Toast.LENGTH_SHORT);
            return;
        }

        CreateAdminOrderRequest request = new CreateAdminOrderRequest(
                "PENDING",
                "CASH",
                mListProduct,
                selectedUser,
                binding.edtShippingAddress.getText().toString().trim(),
                binding.edtRecipientName.getText().toString().trim(),
                binding.edtPhoneNumber.getText().toString().trim()
        );
        orderManagementViewModel.createOrder(request);
    }
}