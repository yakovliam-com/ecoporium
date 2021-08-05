package net.ecoporium.ecoporium.screen.data;

public class TickerScreenInfo {

    /**
     * Width
     */
    private int width;

    /**
     * Height
     */
    private int height;

    /**
     * Screen info
     *
     * @param width  width
     * @param height height
     */
    public TickerScreenInfo(int width, int height) {
        this.width = width;
        this.height = height;
    }

    /**
     * Returns width
     *
     * @return width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Sets width
     *
     * @param width width
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * Returns height
     *
     * @return height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Sets height
     *
     * @param height height
     */
    public void setHeight(int height) {
        this.height = height;
    }
}
