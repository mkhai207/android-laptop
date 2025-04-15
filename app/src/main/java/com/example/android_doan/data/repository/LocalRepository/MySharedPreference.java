package com.example.android_doan.data.repository.LocalRepository;

import android.content.Context;
import android.content.SharedPreferences;

public class MySharedPreference{
    private static final String MY_SHARED_PREFERENCE = "com.example.android_doan.data.repository";
    private Context mContext;

    public MySharedPreference(Context context){
        this.mContext = context;
    }

//    public void setIntValue(String key, int value){
//        SharedPreferences sharedPreferences = mContext.getSharedPreferences(MY_SHARED_PREFERENCE, Context.MODE_PRIVATE);
//        SharedPreferences.Editor edt = sharedPreferences.edit();
//        edt.putInt(key, value);
//        edt.apply();
//    }
//
//    public int getIntValue(String key){
//        SharedPreferences sharedPreferences = mContext.getSharedPreferences(MY_SHARED_PREFERENCE, Context.MODE_PRIVATE);
//        return sharedPreferences.getInt(key, 0);
//    }

    public void setStringValue(String key, String value){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(MY_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor edt = sharedPreferences.edit();
        edt.putString(key, value);
        edt.apply();
    }

    public String getStringValue(String key){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(MY_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, null);
    }

    public void clearSharedPreference(String key){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(MY_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        sharedPreferences.edit().remove(key).apply();
    }

    public void setIntValue(String key, int value){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(MY_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor edt = sharedPreferences.edit();
        edt.putInt(key, value);
        edt.apply();
    }

    public int getIntValue(String key){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(MY_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(key, -1);
    }

}
