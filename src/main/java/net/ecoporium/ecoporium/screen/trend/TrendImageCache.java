package net.ecoporium.ecoporium.screen.trend;

import java.awt.image.BufferedImage;

public class TrendImageCache {

    /**
     * Cached image
     */
    private BufferedImage cachedImage;

    /**
     * Trend image cache
     */
    public TrendImageCache() {
    }

    /**
     * Returns the cached image
     *
     * @return cached image
     */
    public BufferedImage getCachedImage() {
        return cachedImage;
    }

    /**
     * Sets the cached image
     * @param cachedImage cached image
     */
    public void cache(BufferedImage cachedImage) {
        this.cachedImage = cachedImage;
    }
}
