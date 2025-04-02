package com.example.android_doan.view.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.android_doan.R;
import com.example.android_doan.data.repository.LocalRepository.DataLocalManager;
import com.example.android_doan.data.repository.RemoteRepository.ProfileRepository;
import com.example.android_doan.databinding.FragmentProfileBinding;
import com.example.android_doan.view.activity.LoginActivity;
import com.example.android_doan.viewmodel.ProfileViewModel;
import com.example.android_doan.viewmodel.ProfileViewModelFactory;

public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;
    private ProfileViewModel profileViewModel;
    private ProfileRepository repository;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        repository = new ProfileRepository();
        profileViewModel = new ViewModelProvider(this, new ProfileViewModelFactory(repository)).get(ProfileViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupDaTa();
        setupListener();
        handleStatus();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    private void setupListener(){
        binding.layoutOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.action_profileFragment_to_orderHistoryFragment);
            }
        });

        binding.tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLogoutConfirmationDialog();
            }
        });

        binding.tvSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.action_profileFragment_to_settingFragment);
            }
        });
    }

    private void setupDaTa(){
        String userId = DataLocalManager.getUserId();
        profileViewModel.getAccount(userId);
        profileViewModel.getUserInfo().observe(getViewLifecycleOwner(), user -> {
            if (user != null){
                Glide.with(requireContext())
                        .load("http://192.168.50.2:8080/" + user.getAvatar())
                        .error(R.drawable.ic_user)
                        .into(binding.ivAvatar);

                binding.tvUserName.setText(user.getFullName());
                binding.tvUserEmail.setText(user.getEmail());
            }
        });
    }

    private void handleStatus(){
        profileViewModel.getActionResult().observe(getViewLifecycleOwner(), resource -> {
            switch (resource.getStatus()){
                case LOADING:
                    binding.progressBar.setVisibility(View.VISIBLE);
                    break;
                case SUCCESS:
                    binding.progressBar.setVisibility(View.GONE);
                    break;
                case ERROR:
                    binding.progressBar.setVisibility(View.VISIBLE);
                    Toast.makeText(requireContext(), resource.getMessage(),  Toast.LENGTH_SHORT).show();
                    break;
                default:
                    binding.progressBar.setVisibility(View.VISIBLE);
                    break;
            }
        });
    }

    private void showLogoutConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Xác nhận đăng xuất");
        builder.setMessage("Bạn có chắc chắn muốn đăng xuất không?");
        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                profileViewModel.logout();
                Intent intent = new Intent(requireContext(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                requireActivity().finish();
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