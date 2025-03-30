package com.example.android_doan.utils;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DateFormatUtil {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    public static String formatIsoDate(String isoDateTime) {
        ZonedDateTime dateTime = ZonedDateTime.parse(isoDateTime);
        return dateTime.format(FORMATTER);
    }
}