package com.example.android_doan.view.fragment;

import static com.example.android_doan.view.fragment.AddOrUpdateAddressFragment.ADDRESS_KEY;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android_doan.R;
import com.example.android_doan.adapter.AddressAdapter;
import com.example.android_doan.base.BaseViewModelFactory;
import com.example.android_doan.data.model.response.AddressResponse;
import com.example.android_doan.data.repository.LocalRepository.DataLocalManager;
import com.example.android_doan.data.repository.RemoteRepository.AddressRepository;
import com.example.android_doan.databinding.FragmentAddressBinding;
import com.example.android_doan.utils.CustomToast;
import com.example.android_doan.viewmodel.AddressViewModel;

import java.util.ArrayList;

public class AddressFragment extends Fragment {
    public static final String REQUEST_KEY_ADDRESS = "com.example.android_doan.view.fragment.REQUEST_KEY_ADDRESS";
    private FragmentAddressBinding binding;
    private AddressViewModel addressViewModel;
    private RecyclerView rcvAddress;
    private AddressAdapter addressAdapter;
    private Boolean mActionBack = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addressViewModel = new ViewModelProvider(
                this,
                new BaseViewModelFactory<AddressRepository>(new AddressRepository(), AddressViewModel.class)
        ).get(AddressViewModel.class);

        if (getArguments() != null) {
            mActionBack = getArguments().getBoolean("ACTION_BACK_KEY");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddressBinding.inflate(inflater, container, false);
        setupRecyclerView();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        observer();
        setupListener();
        handleStatus();
    }

    @Override
    public void onResume() {
        super.onResume();
        String userId = DataLocalManager.getUserId();
        addressViewModel.refresh("user:'" + userId + "'");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    private void setupRecyclerView() {
        addressAdapter = new AddressAdapter(new ArrayList<>());
        rcvAddress = binding.rcvAddress;
        rcvAddress.setAdapter(addressAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        rcvAddress.setLayoutManager(linearLayoutManager);

        addressAdapter.attachSwipeToDelete(rcvAddress);
    }

    private void observer() {
        String userId = DataLocalManager.getUserId();
        addressViewModel.getAddresses("user:'" + userId + "'");
        addressViewModel.getAddressesLiveData().observe(getViewLifecycleOwner(), addresses -> {
            if (addresses != null) {
                addressAdapter.updateData(addresses);
            }
        });
    }

    private void setupListener() {
        binding.btnAddAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("lkhai4617", "onClickCreatetAddress: ");
                openAddOrUpdateAddressFragment(null);
            }
        });

        addressAdapter.setListener(new AddressAdapter.IOnClickAddress() {
            @Override
            public void onClickEdit(AddressResponse addressResponse) {
                Log.d("lkhai4617", "onClickEditAddress: ");
                openAddOrUpdateAddressFragment(addressResponse);
            }

            @Override
            public void onClickDelete(AddressResponse addressResponse) {
                showLogoutConfirmationDialog(addressResponse);
            }

            @Override
            public void onClickSelect(AddressResponse addressResponse) {
                Bundle result = new Bundle();
                result.putSerializable(ADDRESS_KEY, addressResponse);
                getParentFragmentManager().setFragmentResult(REQUEST_KEY_ADDRESS, result);
                NavHostFragment navHostFragment =
                        (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.frag_container);
                if (navHostFragment != null && mActionBack) {
                    NavController navController = navHostFragment.getNavController();
                    navController.popBackStack();
                }
            }
        });
    }

    private void handleStatus() {
        addressViewModel.getApiResultLiveData().observe(getViewLifecycleOwner(), apiResult -> {
            if (apiResult != null) {
                switch (apiResult.getStatus()) {
                    case LOADING:
                        binding.progressBar.setVisibility(View.VISIBLE);
                        break;
                    case SUCCESS:
                        binding.progressBar.setVisibility(View.GONE);
                        switch (apiResult.getMessage()) {
                            case "deleteAddress":
                                String userId = DataLocalManager.getUserId();
                                addressViewModel.refresh("user:'" + userId + "'");
                                CustomToast.showToast(requireContext(), "Thành công", 2000);
                                break;
                        }
                        break;
                    case ERROR:
                        binding.progressBar.setVisibility(View.GONE);
                        break;
                }
            }
        });
    }

    private void openAddOrUpdateAddressFragment(AddressResponse address) {
        NavHostFragment navHostFragment =
                (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.frag_container);
        if (navHostFragment != null) {
            if (address != null) {
                Bundle args = new Bundle();
                args.putSerializable(ADDRESS_KEY, address);

                NavController navController = navHostFragment.getNavController();
                navController.navigate(R.id.action_addressFragment_to_addOrUpdateAddressFragment, args);
            } else {
                NavController navController = navHostFragment.getNavController();
                navController.navigate(R.id.action_addressFragment_to_addOrUpdateAddressFragment);
            }
        }
    }

    private void showLogoutConfirmationDialog(AddressResponse addressResponse) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Xác nhận xoá");
        builder.setMessage("Bạn có chắc chắn muốn xoá địa chỉ này không?");
        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                addressViewModel.deleteAddress(addressResponse.getId());
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setCancelable(true);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}