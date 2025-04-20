package com.example.android_doan.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android_doan.R;

public class CustomToast {
    public static void showToast(Context context, String message, int duration) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.layout_custom_toast, null);

        TextView tvMessage = layout.findViewById(R.id.tv_toast_message);
        ImageView ivIcon = layout.findViewById(R.id.iv_toast_icon);

        tvMessage.setText(message);
        ivIcon.setImageResource(R.drawable.ic_info);

        Toast toast = new Toast(context);
        toast.setDuration(duration);
        toast.setView(layout);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
