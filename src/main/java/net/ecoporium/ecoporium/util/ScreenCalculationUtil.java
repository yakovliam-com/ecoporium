package net.ecoporium.ecoporium.util;

import net.ecoporium.ecoporium.api.wrapper.Pair;
import net.ecoporium.ecoporium.screen.info.ScreenInfo;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class ScreenCalculationUtil {

    private static final int WIDTH = 128;

    private static final int HEIGHT = 128;

    /**
     * Calculate the number of maps required to create a screen
     *
     * @param screenInfo screen info
     * @return number of maps required
     */
    public static int calculateNumberOfMapsRequired(ScreenInfo screenInfo) {
        int width = screenInfo.getWidth();
        int height = screenInfo.getHeight();

        int widthMaps = getMapsByPixels(width);
        int heightMaps = getMapsByPixels(height);

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

        int widthMaps = getMapsByPixels(width);
        int heightMaps = getMapsByPixels(height);

        // return calculated
        return new Pair<>(widthMaps, heightMaps);
    }

    /**
     * Divide an image into parts
     *
     * @param image image
     * @return parts
     */
    public static List<BufferedImage> divideIntoParts(ScreenInfo screenInfo, BufferedImage image) {
        Pair<Integer, Integer> nonlinearParts = calculateWidthHeightMaps(screenInfo);
        int width = nonlinearParts.getLeft();
        int height = nonlinearParts.getRight();

        List<BufferedImage> result = new ArrayList<>(width * height);

        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                result.add(image.getSubimage(WIDTH * x, HEIGHT * y, WIDTH, HEIGHT));
            }
        }

        return result;
    }
    /**
     * Returns # of maps
     *
     * @param pixels pixels
     * @return maps
     */
    private static int getMapsByPixels(int pixels) {
        int maps = (int) Math.ceil(pixels / 128.0);

        int remainder = pixels % 128;

        if (remainder > 0) {
            maps++;
        }

        return maps;
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
        int widthInPixels = widthInMaps * WIDTH;
        int heightInPixels = heightInMaps * HEIGHT;

        return new ScreenInfo(widthInPixels, heightInPixels);
    }
}
