package com.yakovliam.ecoporium.util;

import java.text.DecimalFormat;

public class NumberUtil {

    private static final DecimalFormat decimalFormat = new DecimalFormat();

    /**
     * Formats a decimal to places
     *
     * @param number number
     * @param places places
     * @return string
     */
    public static String formatToPlaces(float number, int places) {
        decimalFormat.setMaximumFractionDigits(places);
        return decimalFormat.format(number);
    }
}
