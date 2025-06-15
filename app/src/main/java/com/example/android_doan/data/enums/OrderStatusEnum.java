package com.example.android_doan.data.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum OrderStatusEnum {
    PENDING,
    //    UNPAID,
//    PAID,
    SHIPPING,
    COMPLETED,
    CANCELED;

    public static List<String> getAllStatusNames() {
        return Arrays.stream(OrderStatusEnum.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    public static OrderStatusEnum fromString(String status) {
        for (OrderStatusEnum s : OrderStatusEnum.values()) {
            if (s.name().equalsIgnoreCase(status)) {
                return s;
            }
        }
        throw new IllegalArgumentException("No enum constant for status: " + status);
    }
}
