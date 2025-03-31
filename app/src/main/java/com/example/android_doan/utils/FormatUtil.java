package com.example.android_doan.utils;

import java.text.NumberFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.TimeZone;

public class FormatUtil {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final NumberFormat NUMBER_FORMAT = NumberFormat.getNumberInstance(new Locale("vi", "VN"));

    static {
        NUMBER_FORMAT.setGroupingUsed(true);
        NUMBER_FORMAT.setMinimumFractionDigits(0);
    }
    public static String formatIsoDate(String isoDateTime) {
        ZonedDateTime dateTime = ZonedDateTime.parse(isoDateTime);
        return dateTime.format(DATE_FORMATTER);
    }

    public static String formatCurrency(double amount) {
        return NUMBER_FORMAT.format(amount) + " VNĐ";
    }
}