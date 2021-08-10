package net.ecoporium.ecoporium.screen.chart;

import com.github.johnnyjayjay.spigotmaps.util.ImageTools;
import net.ecoporium.ecoporium.market.stock.quote.SimpleStockQuote;
import net.ecoporium.ecoporium.model.factory.Factory;
import net.ecoporium.ecoporium.screen.TrendScreen;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

public class TrendScreenChartFactory implements Factory<TrendScreen, BufferedImage> {

    @Override
    public BufferedImage build(TrendScreen context) {
        // get history
        LinkedList<SimpleStockQuote> history = context.getTicker().getHistory();
        // TODO implement actual chart creating
        return ImageTools.createSingleColoredImage(Color.GREEN);
    }
}
