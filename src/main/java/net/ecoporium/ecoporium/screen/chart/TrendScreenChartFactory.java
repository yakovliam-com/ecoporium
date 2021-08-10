package net.ecoporium.ecoporium.screen.chart;

import net.ecoporium.ecoporium.market.stock.quote.SimpleStockQuote;
import net.ecoporium.ecoporium.model.factory.Factory;
import net.ecoporium.ecoporium.screen.TrendScreen;
import net.ecoporium.ecoporium.screen.info.ScreenInfo;
import net.ecoporium.ecoporium.util.NumberUtil;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.DefaultCategoryItemRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

public class TrendScreenChartFactory implements Factory<TrendScreen, BufferedImage> {

    @Override
    public BufferedImage build(TrendScreen context) {
        // get history
        LinkedList<SimpleStockQuote> history = context.getTicker().getHistory();

        // if there's no history yet, just return a blank screen
        if (history == null || history.size() <= 0) {
            return createSingleColoredImage(context.getScreenInfo(), Color.WHITE);
        }

        // create dataset
        DefaultCategoryDataset defaultCategoryDataset = new DefaultCategoryDataset();

        IntStream.range(0, history.size()).forEach(i -> defaultCategoryDataset.addValue(history.get(i).getPrice(), "Price", Integer.toString(i)));

        JFreeChart chart = ChartFactory.createLineChart(
                context.getTicker().getSymbol() + " ($" + NumberUtil.formatToPlaces(history.getLast().getPrice(), 2) + ")", "Time",
                "Price",
                defaultCategoryDataset, PlotOrientation.VERTICAL,
                true, true, false);

        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.getDomainAxis().setVisible(false);
        plot.getRangeAxis().setRange(getLowestQuote(history) * 0.99, getHighestQuote(history) * 1.01);

        // set background color & legend background to black & title to white & disable grid lines
        chart.setBackgroundPaint(Color.BLACK);
        chart.getTitle().setPaint(Color.WHITE);
        chart.getLegend().setBackgroundPaint(Color.BLACK);
        plot.setBackgroundPaint(Color.BLACK);

        // set grid lines to be 50% transparent white
        plot.setDomainGridlinePaint(new Color(255, 255, 255, 127));
        plot.setRangeGridlinePaint(new Color(255, 255, 255, 127));

        // create renderer
        DefaultCategoryItemRenderer defaultCategoryItemRenderer = new DefaultCategoryItemRenderer();

        // set stroke of line to thick-ish green
        defaultCategoryItemRenderer.setSeriesPaint(0, Color.GREEN);
        defaultCategoryItemRenderer.setSeriesStroke(0, new BasicStroke(5.0f));
        defaultCategoryItemRenderer.setLegendTextPaint(0, Color.WHITE);


        // set renderer
        plot.setRenderer(defaultCategoryItemRenderer);

        return chart.createBufferedImage(context.getScreenInfo().getWidth(), context.getScreenInfo().getHeight());
    }

    /**
     * Create single colored image
     *
     * @param screenInfo screen info
     * @param color      color
     * @return image
     */
    private static BufferedImage createSingleColoredImage(ScreenInfo screenInfo, Color color) {
        BufferedImage image = new BufferedImage(screenInfo.getWidth(), screenInfo.getHeight(), 1);
        Graphics2D graphics = image.createGraphics();
        graphics.setPaint(color);
        graphics.fillRect(0, 0, image.getWidth(), image.getHeight());
        graphics.dispose();
        return image;
    }


    /**
     * Returns the lowest quote given data
     *
     * @param data data
     * @return lowest quote
     */
    private Float getLowestQuote(List<SimpleStockQuote> data) {
        Float lowest = null;

        for (SimpleStockQuote datum : data) {
            if (lowest == null || datum.getPrice() < lowest) {
                lowest = datum.getPrice();
            }
        }

        return lowest;
    }

    /**
     * Returns the highest quote give data
     *
     * @param data data
     * @return highest quote
     */
    private Float getHighestQuote(List<SimpleStockQuote> data) {
        Float highest = null;

        for (SimpleStockQuote datum : data) {
            if (highest == null || datum.getPrice() > highest) {
                highest = datum.getPrice();
            }
        }

        return highest;
    }
}
