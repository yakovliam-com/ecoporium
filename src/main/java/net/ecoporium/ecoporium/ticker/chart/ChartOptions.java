package net.ecoporium.ecoporium.ticker.chart;

public class ChartOptions {

    /**
     * Width
     */
    private final int width;

    /**
     * Height
     */
    private final int height;

    /**
     * Chart options
     *
     * @param width  width
     * @param height height
     */
    public ChartOptions(int width, int height) {
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
     * Returns height
     *
     * @return height
     */
    public int getHeight() {
        return height;
    }
}
