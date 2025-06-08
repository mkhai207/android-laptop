package com.example.android_doan.view.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.example.android_doan.R;
import com.example.android_doan.base.BaseViewModelFactory;
import com.example.android_doan.data.repository.RemoteRepository.AdminHomeRepository;
import com.example.android_doan.data.repository.RemoteRepository.AuthRepository;
import com.example.android_doan.databinding.ActivityAdminBinding;
import com.example.android_doan.databinding.DrawerHeaderBinding;
import com.example.android_doan.viewmodel.AdminHomeViewModel;
import com.example.android_doan.viewmodel.AuthViewModel;
import com.google.android.material.navigation.NavigationView;

public class AdminActivity extends AppCompatActivity {
    private ActivityAdminBinding adminActivityBinding;
    private AuthViewModel authViewModel;
    private AdminHomeViewModel adminHomeViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adminActivityBinding = ActivityAdminBinding.inflate(getLayoutInflater());
        setContentView(adminActivityBinding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.drawer_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();
        setupToolbar();
        setupDataDrawerHeader();
        setupDrawerListener();
        handleActionBack();
    }

    private void setupToolbar() {
        setSupportActionBar(adminActivityBinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        adminActivityBinding.toolbar.setNavigationOnClickListener(v -> {
            NavController navController = Navigation.findNavController(AdminActivity.this, R.id.nav_host_fragment);
            if (!navController.popBackStack()) {
                finish();
            }
        });
    }

    private void setupDrawerListener() {
        //        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        if (navHostFragment != null) {
            NavController navController = navHostFragment.getNavController();
//            NavigationUI.setupWithNavController(activityAdminBinding.bottomNav, navController);

            NavigationUI.setupWithNavController(adminActivityBinding.navView, navController);

//            AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph())
//                    .setOpenableLayout(adminActivityBinding.drawerLayout)
//                    .build();
//            NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
            NavigationUI.setupActionBarWithNavController(this, navController);

            adminActivityBinding.navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    if (item.getItemId() == R.id.nav_logout) {
                        showLogoutConfirmationDialog();
                        return true;
                    }
                    boolean handled = NavigationUI.onNavDestinationSelected(item, navController);
                    if (handled) {
                        adminActivityBinding.drawerLayout.closeDrawer(GravityCompat.START);
                    }
                    return handled;
                }
            });
        }
    }

    private void init() {
        AuthRepository authRepository = new AuthRepository();
        authViewModel = new ViewModelProvider(
                this,
                new BaseViewModelFactory<AuthRepository>(new AuthRepository(), AuthViewModel.class)
        ).get(AuthViewModel.class);

        adminHomeViewModel = new ViewModelProvider(
                this,
                new BaseViewModelFactory<AdminHomeRepository>(new AdminHomeRepository(), AdminHomeViewModel.class)
        ).get(AdminHomeViewModel.class);
    }

    private void handleActionBack() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (adminActivityBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    adminActivityBinding.drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    NavController navController =
                            Navigation.findNavController(AdminActivity.this, R.id.nav_host_fragment);
                    if (!navController.popBackStack()) {
                        finish();
                    }
                }
            }
        });
    }

    private void showLogoutConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xác nhận đăng xuất");
        builder.setMessage("Bạn có chắc chắn muốn đăng xuất không?");
        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                authViewModel.logout();
                Intent intent = new Intent(AdminActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
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

    private void setupDataDrawerHeader() {
        DrawerHeaderBinding headerBinding =
                DrawerHeaderBinding.bind(adminActivityBinding.navView.getHeaderView(0));
        adminHomeViewModel.getUser();
        adminHomeViewModel.getUserLiveData().observe(this, userModel -> {
            if (userModel != null) {
                Glide.with(this)
                        .load(userModel.getAvatar())
                        .error(R.drawable.ic_user)
                        .into(headerBinding.imgAvatar);
                headerBinding.tvFullName.setText(userModel.getFullName());
            }
        });
    }
}