package com.example.android_doan.data.enums;

import android.util.Log;

public enum RoleEnum {
    ADMIN("SUPER_ADMIN"),
    CUSTOMER("CUSTOMER"),
    STAFF("STAFF"),
    UNKNOWN("UNKNOWN");

    private final String code;

    RoleEnum(String code) {
        this.code = code;
    }

    public static RoleEnum fromString(String code) {
        if (code != null) {
            for (RoleEnum role : RoleEnum.values()) {
                if (role.code.equalsIgnoreCase(code)) {
                    return role;
                }
            }
        }
        Log.e("RoleEnum", "Invalid role code: " + code);
        return UNKNOWN;
    }

    public String getCode() {
        return code;
    }
}
