package com.example.android_doan.adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.android_doan.data.enums.OrderStatusEnum;
import com.example.android_doan.view.fragment.OrderStatusFragment;

public class OrderViewPager2Adapter extends FragmentStateAdapter {

    public OrderViewPager2Adapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        OrderStatusFragment fragment = new OrderStatusFragment();
        Bundle args = new Bundle();
        args.putString("status", OrderStatusEnum.values()[position].name());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getItemCount() {
        return OrderStatusEnum.values().length;
    }
}
