package com.example.android_doan.data.enums;

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
        try {
            return RoleEnum.valueOf(code.toUpperCase());
        } catch (IllegalArgumentException e) {
            return UNKNOWN;
        }
    }

    public String getCode() {
        return code;
    }
}
