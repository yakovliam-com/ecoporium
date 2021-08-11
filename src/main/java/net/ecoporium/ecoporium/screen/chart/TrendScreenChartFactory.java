package net.ecoporium.ecoporium.screen.chart;

import net.ecoporium.ecoporium.EcoporiumPlugin;
import net.ecoporium.ecoporium.api.config.generic.adapter.ConfigurationAdapter;
import net.ecoporium.ecoporium.api.wrapper.Pair;
import net.ecoporium.ecoporium.market.stock.HistoricalAnalysis;
import net.ecoporium.ecoporium.market.stock.quote.SimpleStockQuote;
import net.ecoporium.ecoporium.model.factory.Factory;
import net.ecoporium.ecoporium.screen.TrendScreen;
import net.ecoporium.ecoporium.util.ChartUtil;
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

import static net.ecoporium.ecoporium.config.EcoporiumConfigKeys.*;

public class TrendScreenChartFactory implements Factory<TrendScreen, BufferedImage> {

    /**
     * Configuration adapter
     */
    private final ConfigurationAdapter configurationAdapter;

    /**
     * Trend screen chart factory
     *
     * @param plugin plugin
     */
    public TrendScreenChartFactory(EcoporiumPlugin plugin) {
        /**
         * Ecoporium plugin
         */
        this.configurationAdapter = plugin.getEcoporiumConfig().getAdapter();
    }

    @Override
    public BufferedImage build(TrendScreen context) {
        // get history
        LinkedList<SimpleStockQuote> history = context.getTicker().getHistory();
        // get historical analysis
        HistoricalAnalysis historicalAnalysis = context.getTicker().getHistoricalAnalysis();

        // if there's no history yet, just return a blank screen (aka we need at least two data-points)
        if (history == null || history.size() <= 0) {
            return ChartUtil.createLoadingImage(context.getScreenInfo());
        }

        // create dataset
        DefaultCategoryDataset defaultCategoryDataset = new DefaultCategoryDataset();

        IntStream.range(0, history.size()).forEach(i -> defaultCategoryDataset.addValue(history.get(i).getPrice(), "Price", Integer.toString(i)));

        JFreeChart chart = ChartFactory.createLineChart(
                CHART_TITLE.get(configurationAdapter)
                        .replace("%symbol%", context.getTicker().getSymbol())
                        .replace("%price%", NumberUtil.formatToPlaces(history.getLast().getPrice(), 2)),
                CHART_CATEGORY_AXIS_LABEL.get(configurationAdapter),
                CHART_VALUE_AXIS_LABEL.get(configurationAdapter),
                defaultCategoryDataset, PlotOrientation.VERTICAL,
                true, true, false);

        CategoryPlot plot = (CategoryPlot) chart.getPlot();

        // set axis visible
        plot.getDomainAxis().setVisible(CHART_DOMAIN_AXIS_VISIBLE.get(configurationAdapter));

        // set multiplier (scale)
        Pair<Double, Double> multiplier = CHART_RANGE_LOWER_HIGHER.get(configurationAdapter);
        plot.getRangeAxis().setRange(getLowestQuote(history) * multiplier.getLeft(),
                getHighestQuote(history) * multiplier.getRight());

        // set background color & legend background to black & title to white & disable grid lines
        chart.setBackgroundPaint(CHART_CHART_BACKGROUND_PAINT.get(configurationAdapter));
        chart.getTitle().setPaint(CHART_CHART_TITLE_PAINT.get(configurationAdapter));
        chart.getLegend().setBackgroundPaint(CHART_CHART_LEGEND_BACKGROUND_PAINT.get(configurationAdapter));
        plot.setBackgroundPaint(CHART_PLOT_BACKGROUND_PAINT.get(configurationAdapter));

        // set grid lines to be 50% transparent white
        plot.setDomainGridlinePaint(CHART_PLOT_DOMAIN_GRIDLINE_PAINT.get(configurationAdapter));
        plot.setRangeGridlinePaint(CHART_PLOT_RANGE_GRIDLINE_PAINT.get(configurationAdapter));

        // create renderer
        DefaultCategoryItemRenderer defaultCategoryItemRenderer = new DefaultCategoryItemRenderer();

        Color lineColor = switch (historicalAnalysis) {
            case GOING_UP -> CHART_STOCK_GOING_UP_COLOR.get(configurationAdapter);
            case GOING_DOWN -> CHART_STOCK_GOING_DOWN_COLOR.get(configurationAdapter);
            default -> CHART_STOCK_NEUTRAL_COLOR.get(configurationAdapter);
        };

        // set stroke of line to thick-ish green
        defaultCategoryItemRenderer.setSeriesPaint(0, lineColor);
        defaultCategoryItemRenderer.setSeriesStroke(0, new BasicStroke(CHART_STOCK_LINE_THICKNESS.get(configurationAdapter).floatValue()));
        defaultCategoryItemRenderer.setLegendTextPaint(0, CHART_CHART_LEGEND_TEXT_PAINT.get(configurationAdapter));


        // set renderer
        plot.setRenderer(defaultCategoryItemRenderer);

        // resize
        return chart.createBufferedImage(context.getScreenInfo().getWidth(), context.getScreenInfo().getHeight());
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
