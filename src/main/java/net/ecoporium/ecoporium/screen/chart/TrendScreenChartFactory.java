package net.ecoporium.ecoporium.screen.chart;

import net.ecoporium.ecoporium.market.stock.quote.SimpleStockQuote;
import net.ecoporium.ecoporium.model.factory.Factory;
import net.ecoporium.ecoporium.screen.TrendScreen;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import java.awt.image.BufferedImage;
import java.util.LinkedList;

public class TrendScreenChartFactory implements Factory<TrendScreen, BufferedImage> {

    @Override
    public BufferedImage build(TrendScreen context) {
        // get history
        LinkedList<SimpleStockQuote> history = context.getTicker().getHistory();

        // create dataset
        DefaultCategoryDataset defaultCategoryDataset = new DefaultCategoryDataset();
        for (int i = 0; i < history.size(); i++) {
            SimpleStockQuote quote = history.get(i);
            defaultCategoryDataset.addValue(quote.getPrice(), "Price", Integer.toString(i));
        }

        // TODO change background to black
        // TODO change line to green
        // TODO make line thicker
        // TODO set scale

        JFreeChart chart = ChartFactory.createLineChart(
                context.getTicker().getSymbol(), "Time",
                "Price",
                defaultCategoryDataset, PlotOrientation.VERTICAL,
                true, true, false);
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.getDomainAxis().setVisible(false);

        return chart.createBufferedImage(context.getScreenInfo().getWidth(), context.getScreenInfo().getHeight());
    }
}
