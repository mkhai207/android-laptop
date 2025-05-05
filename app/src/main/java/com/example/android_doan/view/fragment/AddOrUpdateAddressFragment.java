package com.example.android_doan.view.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android_doan.R;
import com.example.android_doan.data.model.UserModel;
import com.example.android_doan.data.model.request.CreateAddressRequest;
import com.example.android_doan.data.model.response.AddressResponse;
import com.example.android_doan.data.repository.LocalRepository.DataLocalManager;
import com.example.android_doan.data.repository.RemoteRepository.AddressRepository;
import com.example.android_doan.databinding.FragmentAddOrUpdateAddressBinding;
import com.example.android_doan.utils.CustomToast;
import com.example.android_doan.viewmodel.AddressViewModel;
import com.example.android_doan.viewmodel.AddressViewModelFactory;

public class AddOrUpdateAddressFragment extends Fragment {
    public static final String ADDRESS_KEY = "com.example.android_doan.view.fragment.ADDRESS_KEY";
    private FragmentAddOrUpdateAddressBinding binding;
    private AddressViewModel addressViewModel;
    private AddressResponse addressResponse;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null){
            addressResponse = (AddressResponse) args.getSerializable(ADDRESS_KEY);
        }

        AddressRepository addressRepository = new AddressRepository();
        addressViewModel =
                new ViewModelProvider(this, new AddressViewModelFactory(addressRepository)).get(AddressViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddOrUpdateAddressBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupData();
        setupListener();
        handleStatus();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    private void setupData(){
        if (addressResponse != null){
            binding.etRecipientName.setText(addressResponse.getRecipientName());
            binding.etPhoneNumber.setText(addressResponse.getPhoneNumber());
            binding.etStreet.setText(addressResponse.getStreet());
            binding.etWard.setText(addressResponse.getWard());
            binding.etDistrict.setText(addressResponse.getDistrict());
            binding.etCity.setText(addressResponse.getCity());
            binding.cbIsDefault.setChecked(addressResponse.isDefault());
        }
    }

    private void setupListener(){
        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (addressResponse != null){
                    updateAddress();
                } else {
                    createAddress();
                }
            }
        });
    }
    private void handleStatus(){
        addressViewModel.getApiResultLiveData().observe(getViewLifecycleOwner(), apiResult -> {
            if (apiResult != null){
                switch(apiResult.getStatus()){
                    case LOADING:
                        binding.progressBar.setVisibility(View.VISIBLE);
                        break;
                    case SUCCESS:
                        binding.progressBar.setVisibility(View.GONE);
                        switch (apiResult.getMessage()){
                            case "createAddress":
                                requireActivity().getSupportFragmentManager().popBackStack();
                                break;
                            case "updateAddress":
                                requireActivity().getSupportFragmentManager().popBackStack();
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


    private void updateAddress(){
        String recipientName = binding.etRecipientName.getText().toString();
        String phone = binding.etPhoneNumber.getText().toString();
        String street = binding.etStreet.getText().toString();
        String ward = binding.etWard.getText().toString();
        String district = binding.etDistrict.getText().toString();
        String city = binding.etCity.getText().toString();
        boolean isDefault = binding.cbIsDefault.isChecked();
        String userId = DataLocalManager.getUserId();
        UserModel userModel = new UserModel(Integer.parseInt(userId));
        AddressResponse address =
                new AddressResponse(
                        addressResponse.getId(),
                        recipientName,
                        phone,
                        street,
                        ward,
                        district,
                        city,
                        isDefault,
                        userModel
                );
        addressViewModel.updateAddress(address);
    }

    private void createAddress(){
        String recipientName = binding.etRecipientName.getText().toString();
        String phone = binding.etPhoneNumber.getText().toString();
        String street = binding.etStreet.getText().toString();
        String ward = binding.etWard.getText().toString();
        String district = binding.etDistrict.getText().toString();
        String city = binding.etCity.getText().toString();
        boolean isDefault = binding.cbIsDefault.isChecked();
        String userId = DataLocalManager.getUserId();
        UserModel userModel = new UserModel(Integer.parseInt(userId));
        CreateAddressRequest createAddressRequest =
                new CreateAddressRequest(
                        recipientName,
                        phone,
                        street,
                        ward,
                        district,
                        city,
                        isDefault,
                        userModel
                );
        addressViewModel.createAddress(createAddressRequest);
    }
}