package com.example.android_doan.view.activity;

import android.os.Bundle;
import android.view.Menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.android_doan.R;
import com.example.android_doan.base.BaseViewModelFactory;
import com.example.android_doan.data.repository.RemoteRepository.CartRepository;
import com.example.android_doan.databinding.ActivityHomeBinding;
import com.example.android_doan.viewmodel.CartViewModel;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {
    private NavController navController;
    private BottomNavigationView bottomNav;
    private ActivityHomeBinding binding;
    private CartViewModel cartViewModel;
    private BadgeDrawable cartBadge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        setSupportActionBar(binding.toolbar);

        // setup cart viewmodel
        cartViewModel = new ViewModelProvider(
                this,
                new BaseViewModelFactory<CartRepository>(new CartRepository(), CartViewModel.class)
        ).get(CartViewModel.class);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.frag_container);
        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();
            AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
            NavigationUI.setupWithNavController(binding.toolbar, navController, appBarConfiguration);
            NavigationUI.setupWithNavController(binding.bottomNav, navController);
        }

        bottomNav = binding.bottomNav;
        // Khởi tạo badge cho item Cart
        cartBadge = bottomNav.getOrCreateBadge(R.id.cartFragment);
        cartBadge.setVisible(false);
        cartBadge.setBackgroundColor(getResources().getColor(R.color.red));
        cartBadge.setBadgeTextColor(getResources().getColor(R.color.white));

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            int destinationId = destination.getId();
            switch (destinationId) {
                case R.id.homeFragment:
                    bottomNav.getMenu().findItem(R.id.homeFragment).setChecked(true);
                    break;
                case R.id.cartFragment:
                    bottomNav.getMenu().findItem(R.id.cartFragment).setChecked(true);
                    break;
                case R.id.profileFragment:
                    bottomNav.getMenu().findItem(R.id.profileFragment).setChecked(true);
                    break;
            }
        });
        cartViewModel.getItemCartLiveData().observe(this, carts -> {
            updateCartBadge(carts.size());
        });
        cartViewModel.getCart();
    }

    public void updateCartBadge(int count) {
        if (count > 0) {
            cartBadge.setNumber(count);
            cartBadge.setVisible(true);
        } else {
            cartBadge.setVisible(false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp() || super.onSupportNavigateUp();
    }
}