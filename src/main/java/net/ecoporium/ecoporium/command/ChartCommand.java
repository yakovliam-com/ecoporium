package net.ecoporium.ecoporium.command;

import co.aikar.commands.annotation.CatchUnknown;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Single;
import net.ecoporium.ecoporium.EcoporiumPlugin;
import net.ecoporium.ecoporium.api.message.Message;
import org.bukkit.entity.Player;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import yahoofinance.histquotes.HistoricalQuote;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;
import java.util.Random;

@CommandAlias("chart")
public class ChartCommand extends EcoporiumCommand {

    /**
     * Ecoporium command
     *
     * @param plugin plugin
     */
    public ChartCommand(EcoporiumPlugin plugin) {
        super(plugin);
    }

    @Default
    public void onChart(Player player, @Single String market, @Single String symbol) {
        Message.builder()
                .addLine("&7Outputting chart for &f" + symbol)
                .build()
                .message(player);

        getPlugin().getMarketCache().get(market, null).getTicker(symbol).get().thenAccept((stock) -> {
            Map<Calendar, HistoricalQuote> history = stock.getPreviousHistory();
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE, dd/MM/yyyy");

            // create data
            history.forEach((date, quote) -> {
                dataset.addValue(quote.getClose().doubleValue(), "Stock Price", simpleDateFormat.format(date.getTime()));
            });

            // create jchart

            JFreeChart chart = ChartFactory.createLineChart(stock.getSymbol(), "Time", "Price", dataset);

            int width = 640;    /* Width of the image */
            int height = 480;   /* Height of the image */
            File lineChart = new File(System.getProperty("user.dir"), stock.getSymbol() + "-History-Chart.jpeg");
            try {
                ChartUtils.saveChartAsJPEG(lineChart, chart, width, height);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
