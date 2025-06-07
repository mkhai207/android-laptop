package com.example.android_doan.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

import com.example.android_doan.R;
import com.example.android_doan.databinding.FragmentOrderSuccessBinding;

public class OrderSuccessFragment extends Fragment {
    private FragmentOrderSuccessBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentOrderSuccessBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupListener();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    private void setupListener() {
        binding.btnContinueShopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment navHostFragment =
                        (NavHostFragment) requireActivity().getSupportFragmentManager()
                                .findFragmentById(R.id.frag_container);
                if (navHostFragment != null) {
                    NavController navController = navHostFragment.getNavController();

                    NavOptions navOptions = new NavOptions.Builder()
                            .setPopUpTo(R.id.homeFragment, true)
                            .setLaunchSingleTop(true)
                            .build();
                    navController.navigate(R.id.homeFragment, null, navOptions);
                }
            }
        });
    }
}