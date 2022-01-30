package com.yakovliam.ecoporium.util;

import java.awt.*;

public class ColorUtil {

    /**
     * Parses string input into a color
     * e.g. 255,255,255,127 turns into Color(255,255,255,50% transparent)
     *
     * @param input input
     * @return color
     */
    public static Color parseColor(String input) {
        if (!input.contains(",")) {
            return null;
        }

        String[] parts = input.split(",");

        if (parts.length < 3) {
            return null;
        }

        int r = Integer.parseInt(parts[0]);
        int g = Integer.parseInt(parts[1]);
        int b = Integer.parseInt(parts[2]);
        int a = 255;

        if (parts.length >= 4) {
            a = Integer.parseInt(parts[3]);
        }

        return new Color(r, g, b, a);
    }
}
