package com.example.android_doan.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.android_doan.R;
import com.example.android_doan.data.repository.LocalRepository.DataLocalManager;
import com.example.android_doan.viewmodel.AuthViewModel;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

public class MainActivity extends AppCompatActivity {
    private InterstitialAd mInterstitialAd;
    private static final String TAG = "MainActivity";

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

        // init google mobile ads SDK
        MobileAds.initialize(this, initializationStatus -> {
            loadInterstitialAd();
        });

        // phan quyen login
//        new Handler(Looper.getMainLooper()).postDelayed(()->{
//            if (mInterstitialAd != null) {
//                mInterstitialAd.show(MainActivity.this);
//                mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
//                    @Override
//                    public void onAdDismissedFullScreenContent() {
//                        navigateToNextScreen();
//                        mInterstitialAd = null;
//                    }
//
//                    @Override
//                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
//                        Log.e(TAG, "Ad failed to show: " + adError.getMessage());
//                        navigateToNextScreen();
//                        mInterstitialAd = null;
//                    }
//
//                    @Override
//                    public void onAdShowedFullScreenContent() {
//                        Log.i(TAG, "Ad showed full screen content");
//                    }
//                });
//            } else {
//                navigateToNextScreen();
//            }
//        }, 1000);
    }

    private void navigateToNextScreen(){
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
    }

    private void loadInterstitialAd(){
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(this,"ca-app-pub-3940256099942544/1033173712", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        mInterstitialAd = interstitialAd;
                        Log.i(TAG, "onAdLoaded");
                        showInterstitialAd();
                    }
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        Log.d(TAG, loadAdError.toString());
                        mInterstitialAd = null;
                        showInterstitialAd();
                    }
                });
    }

    private void showInterstitialAd() {
        if (mInterstitialAd != null) {
            mInterstitialAd.show(MainActivity.this);
            mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    Log.i(TAG, "Ad dismissed");
                    navigateToNextScreen();
                    mInterstitialAd = null;
                }

                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                    Log.e(TAG, "Ad failed to show: " + adError.getMessage());
                    navigateToNextScreen();
                    mInterstitialAd = null;
                }

                @Override
                public void onAdShowedFullScreenContent() {
                    Log.i(TAG, "Ad showed full screen content");
                }
            });
        } else {
            navigateToNextScreen();
        }
    }
}