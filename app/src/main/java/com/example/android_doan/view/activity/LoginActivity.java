package com.example.android_doan.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.android_doan.R;
import com.example.android_doan.base.BaseViewModelFactory;
import com.example.android_doan.data.repository.RemoteRepository.AuthRepository;
import com.example.android_doan.viewmodel.AuthViewModel;

public class LoginActivity extends AppCompatActivity {
    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // init viewmodel
        authViewModel = new ViewModelProvider(
                this,
                new BaseViewModelFactory<AuthRepository>(new AuthRepository(), AuthViewModel.class)
        ).get(AuthViewModel.class);

//        authViewModel.getUserLiveData().observe(this, userModel -> {
//            Intent intent = new Intent(this, HomeActivity.class);
//            Bundle bundle = new Bundle();
//            bundle.putSerializable("user_key", userModel);
//            intent.putExtras(bundle);
//            if (userModel == null){
//                Log.d("lkhai4617", "null");
//            }
//            startActivity(intent);
//        });
        authViewModel.getUserLiveData().observe(this, userModel -> {
            if (userModel != null) {
                Intent intent = new Intent();
                switch (userModel.getRole().getCode()) {
                    case "CUSTOMER":
                        intent.setClass(this, HomeActivity.class);
                        break;
                    case "SUPER_ADMIN":
                        intent.setClass(this, AdminActivity.class);
                        break;
                    default:
                        break;
                }
                Bundle bundle = new Bundle();
                bundle.putSerializable("user_key", userModel);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        authViewModel.getErrorLiveData().observe(this, error -> {
            Log.d("lkhai4617", error);
            Toast.makeText(this, error, Toast.LENGTH_LONG).show();
        });

        authViewModel.getStatusRegister().observe(this, isSuccess -> {
            getSupportFragmentManager().popBackStack();
        });
    }
}