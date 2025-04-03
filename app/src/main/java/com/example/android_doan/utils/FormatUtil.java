package com.example.android_doan.utils;

import java.text.NumberFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class FormatUtil {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSX");
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
    public static double parseCurrency(String currency) throws NumberFormatException {
        String number = currency.replaceAll("[^0-9]", "");
        return Double.parseDouble(number);
    }

    public static String formatToIsoDate(String displayDate) {
        if (displayDate == null || displayDate.isEmpty()) {
            return null;
        }
        try {
            ZonedDateTime dateTime = ZonedDateTime.parse(
                    displayDate + " 00:00:00Z",
                    DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ssX")
            );
            return dateTime.format(ISO_FORMATTER);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}