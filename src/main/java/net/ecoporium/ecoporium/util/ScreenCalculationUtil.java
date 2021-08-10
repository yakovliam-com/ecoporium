package net.ecoporium.ecoporium.util;

import net.ecoporium.ecoporium.screen.info.ScreenInfo;

public class ScreenCalculationUtil {

    /**
     * Calculate the number of maps required to create a screen
     *
     * @param screenInfo screen info
     * @return number of maps required
     */
    public static int calculateNumberOfMapsRequired(ScreenInfo screenInfo) {
        int width = screenInfo.getWidth();
        int height = screenInfo.getHeight();

        int widthMaps = (int) Math.ceil(width / 128.0);
        int heightMaps = (int) Math.ceil(height / 128.0);

        return widthMaps * heightMaps;
    }
}
