package com.example.android_doan;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.android_doan.data.repository.LocalRepository.DataLocalManager;
import com.example.android_doan.view.activity.AdminActivity;
import com.example.android_doan.view.activity.HomeActivity;
import com.example.android_doan.view.activity.LoginActivity;
import com.example.android_doan.viewmodel.AuthViewModel;

public class MainActivity extends AppCompatActivity {
    private AuthViewModel authViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        new Handler(Looper.getMainLooper()).postDelayed(()->{
            String accessToken = DataLocalManager.getAccessToken();
            String role = DataLocalManager.getRole();
            if (accessToken != null && !accessToken.isEmpty()){
                switch (role){
                    case "CUSTOMER":
                        startActivity(new Intent(this, HomeActivity.class));
                        break;
                    case "SUPER_ADMIN":
                        startActivity(new Intent(this, AdminActivity.class));
                        break;
                    default:
                         break;
                }
            } else {
                startActivity(new Intent(this, LoginActivity.class));
            }
        }, 1000);

    }
}