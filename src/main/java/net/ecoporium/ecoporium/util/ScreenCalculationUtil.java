package net.ecoporium.ecoporium.util;

import net.ecoporium.ecoporium.api.wrapper.Pair;
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

    /**
     * Calculates the number of maps for specifically the width and height
     *
     * @param screenInfo screen info
     * @return number of maps
     */
    public static Pair<Integer, Integer> calculateWidthHeightMaps(ScreenInfo screenInfo) {
        int width = screenInfo.getWidth();
        int height = screenInfo.getHeight();

        int widthMaps = (int) Math.ceil(width / 128.0);
        int heightMaps = (int) Math.ceil(height / 128.0);

        // return calculated
        return new Pair<>(widthMaps, heightMaps);
    }

    /**
     * Parses dimensions (e.g. 5x5 or 7x7) into a pair of map sizes
     *
     * @param dimensions dimensions
     * @return map numbers
     */
    public static Pair<Integer, Integer> parseDimensions(String dimensions) {
        if (!dimensions.contains("x")) {
            return null;
        }

        String[] parts = dimensions.split("x");

        try {
            Integer first = Integer.parseInt(parts[0]);
            Integer second = Integer.parseInt(parts[1]);

            return new Pair<>(first, second);
        } catch (NumberFormatException ignored) {
            return null;
        }
    }

    /**
     * Constructs screen info from map sizes
     *
     * @param mapSizes map sizes
     * @return screen info
     */
    public static ScreenInfo constructFromMapSizeDimensions(Pair<Integer, Integer> mapSizes) {
        int widthInMaps = mapSizes.getLeft();
        int heightInMaps = mapSizes.getRight();

        // get width / height in pixels
        int widthInPixels = widthInMaps * 128;
        int heightInPixels = heightInMaps * 128;

        return new ScreenInfo(widthInPixels, heightInPixels);
    }
}
