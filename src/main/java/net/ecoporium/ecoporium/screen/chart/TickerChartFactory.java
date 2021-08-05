package net.ecoporium.ecoporium.screen.chart;

import net.ecoporium.ecoporium.api.wrapper.Pair;
import net.ecoporium.ecoporium.model.market.StockTicker;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import yahoofinance.quotes.stock.StockQuote;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class TickerChartFactory implements ChartFactory<Pair<StockTicker, ChartOptions>> {

    @Override
    public BufferedImage create(Pair<StockTicker, ChartOptions> context) {
        StockTicker ticker = context.getLeft();
        ChartOptions options = context.getRight();

        // get data
        Map<Date, StockQuote> quoteMap = ticker.getHistory();

        // create data set
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // date format
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");

        // create data
        quoteMap.forEach((date, quote) -> dataset.addValue(quote.getPrice(), "Stock Price", simpleDateFormat.format(date)));

        // create jchart
        JFreeChart chart = org.jfree.chart.ChartFactory.createLineChart(ticker.getSymbol(), "Time", "Price", dataset);
        chart.getCategoryPlot().getRangeAxis().setRange(getLowestQuote(quoteMap) * 0.99, getHighestQuote(quoteMap) * 1.01);

        // create output
        int width = options.getWidth();    /* Width of the image */
        int height = options.getHeight();   /* Height of the image */
        File lineChart = new File(System.getProperty("user.dir") + File.separator + "cache", ticker.getSymbol() + "-Chart.jpeg");
        try {
            ChartUtils.saveChartAsJPEG(lineChart, chart, width, height);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        try {
            return ImageIO.read(lineChart);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Returns the lowest quote given data
     *
     * @param data data
     * @return lowest quote
     */
    private Double getLowestQuote(Map<Date, StockQuote> data) {
        Double lowest = null;

        for (StockQuote value : data.values()) {
            if (lowest == null || value.getPrice().doubleValue() < lowest) {
                lowest = value.getPrice().doubleValue();
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
    private Double getHighestQuote(Map<Date, StockQuote> data) {
        Double highest = null;

        for (StockQuote value : data.values()) {
            if (highest == null || value.getPrice().doubleValue() > highest) {
                highest = value.getPrice().doubleValue();
            }
        }

        return highest;
    }
}
