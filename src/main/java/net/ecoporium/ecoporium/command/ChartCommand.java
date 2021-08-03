package net.ecoporium.ecoporium.command;

import co.aikar.commands.BukkitCommandCompletionContext;
import co.aikar.commands.CommandCompletions;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Single;
import com.github.johnnyjayjay.spigotmaps.RenderedMap;
import com.github.johnnyjayjay.spigotmaps.rendering.AnimatedTextRenderer;
import com.github.johnnyjayjay.spigotmaps.rendering.GifImage;
import com.github.johnnyjayjay.spigotmaps.rendering.GifRenderer;
import com.github.johnnyjayjay.spigotmaps.rendering.ImageRenderer;
import com.github.johnnyjayjay.spigotmaps.util.ImageTools;
import net.ecoporium.ecoporium.EcoporiumPlugin;
import net.ecoporium.ecoporium.api.message.Message;
import net.ecoporium.ecoporium.model.market.Market;
import net.ecoporium.ecoporium.model.market.StockTicker;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapView;
import org.checkerframework.checker.units.qual.A;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.imagemap.ImageMapUtils;
import org.jfree.data.category.DefaultCategoryDataset;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.quotes.stock.StockQuote;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@CommandAlias("chart")
public class ChartCommand extends EcoporiumCommand {


    /**
     * Ecoporium command
     *
     * @param manager manager
     * @param plugin  plugin
     */
    public ChartCommand(CommandManager manager, EcoporiumPlugin plugin) {
        super(manager, plugin);
    }

    @Override
    protected void registerCompletions() {
        CommandCompletions<BukkitCommandCompletionContext> commandCompletions = manager.getCommandCompletions();

        commandCompletions.registerAsyncCompletion("market", c -> plugin.getMarketCache().getMap().values().stream()
                .map(Market::getHandle)
                .collect(Collectors.toList()));
    }

    @Default
    public void onChart(Player player, @Single Market market, @Single String symbol) {
        Message.builder()
                .addLine("&7Rendering live chart for &f" + symbol + " &7in the &f" + market.getHandle() + " &7market.")
                .build()
                .message(player);

        // get stock
        StockTicker ticker = market.getTicker(symbol).get().join();

        // create image first
        ticker.update().join();

        // generate chart
        File lineChart = saveChartWithNewData(ticker);

        // get buffered image
        BufferedImage image;
        try {
            image = ImageIO.read(lineChart);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // TODO do something with the image
    }

    private File saveChartWithNewData(StockTicker ticker) {
        // generate chart
        Map<Date, StockQuote> history = ticker.getHistory();
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");

        // create data
        history.forEach((date, quote) -> {
            dataset.addValue(quote.getPrice(), "Stock Price", simpleDateFormat.format(date));
        });

        // create jchart
        JFreeChart chart = ChartFactory.createLineChart(ticker.getSymbol(), "Time", "Price", dataset);

        int width = 400;    /* Width of the image */
        int height = 300;   /* Height of the image */
        File lineChart = new File(System.getProperty("user.dir"), ticker.getSymbol() + "-History-Chart.jpeg");
        try {
            ChartUtils.saveChartAsJPEG(lineChart, chart, width, height);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return lineChart;
    }
}
