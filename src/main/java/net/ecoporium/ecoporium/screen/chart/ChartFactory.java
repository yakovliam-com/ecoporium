package net.ecoporium.ecoporium.screen.chart;

import java.awt.image.BufferedImage;

public interface ChartFactory<C> {

    /**
     * Creates a buffered image of the applicable chart from context
     *
     * @param context context
     * @return image
     */
    BufferedImage create(C context);
}
