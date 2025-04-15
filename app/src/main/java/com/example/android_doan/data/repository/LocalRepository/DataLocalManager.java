package com.example.android_doan.data.repository.LocalRepository;

import android.content.Context;

public class DataLocalManager {
    private static final String ACCESS_TOKEN = "com.example.android_doan.data.repository.ACCESS_TOKEN";
    private static final String REFRESH_TOKEN = "com.example.android_doan.data.repository.REFRESH_TOKEN";
    private static final String USER_ID = "com.example.android_doan.data.repository.USER_ID";
    private static final String ROLE = "com.example.android_doan.data.repository.ROLE";
    private static DataLocalManager instance;
    private MySharedPreference mySharedPreference;

    public static void init(Context context){
        instance = new DataLocalManager();
        instance.mySharedPreference = new MySharedPreference(context);
    }

    public static DataLocalManager getInstance(){
        if (instance == null){
            instance = new DataLocalManager();
        }
         return instance;
    }

    public static void saveRole(String role){
        DataLocalManager.getInstance().mySharedPreference.setStringValue(ROLE, role);
    }

    public static String getRole(){
        return DataLocalManager.getInstance().mySharedPreference.getStringValue(ROLE);
    }

    public static void saveUserId(String userId){
        DataLocalManager.getInstance().mySharedPreference.setStringValue(USER_ID, userId);
    }

    public static String getUserId(){
        return DataLocalManager.getInstance().mySharedPreference.getStringValue(USER_ID);
    }

    public static void saveAccessToken(String accessToken){
        DataLocalManager.getInstance().mySharedPreference.setStringValue(ACCESS_TOKEN, accessToken);
    }

    public static String getAccessToken(){
        return DataLocalManager.getInstance().mySharedPreference.getStringValue(ACCESS_TOKEN);
    }

    public static void saveRefreshToken(String refreshToken){
        DataLocalManager.getInstance().mySharedPreference.setStringValue(REFRESH_TOKEN, refreshToken);
    }

    public static String getRefreshToken(){
        return DataLocalManager.getInstance().mySharedPreference.getStringValue(REFRESH_TOKEN);
    }

    public static void clearAccessToken(){
        DataLocalManager.getInstance().mySharedPreference.clearSharedPreference(ACCESS_TOKEN);
    }

    public static void clearRefreshToken(){
        DataLocalManager.getInstance().mySharedPreference.clearSharedPreference(REFRESH_TOKEN);
    }

    public static void clearUserId(){
        DataLocalManager.getInstance().mySharedPreference.clearSharedPreference(USER_ID);
    }
    public static void clearRole(){
        DataLocalManager.getInstance().mySharedPreference.clearSharedPreference(ROLE);
    }
}
