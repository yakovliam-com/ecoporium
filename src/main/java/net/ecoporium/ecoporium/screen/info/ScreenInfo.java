package net.ecoporium.ecoporium.screen.info;

public class ScreenInfo {

    /**
     * Screen width in pixels
     */
    private final int width;

    /**
     * Screen height in pixels
     */
    private final int height;

    /**
     * Screen info
     *
     * @param width  width
     * @param height height
     */
    public ScreenInfo(int width, int height) {
        this.width = width;
        this.height = height;
    }

    /**
     * Width
     *
     * @return width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Height
     *
     * @return height
     */
    public int getHeight() {
        return height;
    }
}
