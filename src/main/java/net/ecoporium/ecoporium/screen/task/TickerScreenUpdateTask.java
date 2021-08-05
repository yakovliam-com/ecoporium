package net.ecoporium.ecoporium.screen.task;

import com.github.johnnyjayjay.spigotmaps.util.ImageTools;
import net.ecoporium.ecoporium.EcoporiumPlugin;
import net.ecoporium.ecoporium.api.wrapper.Pair;
import net.ecoporium.ecoporium.market.StockTicker;
import net.ecoporium.ecoporium.task.RepeatingTask;
import net.ecoporium.ecoporium.screen.TickerScreen;
import net.ecoporium.ecoporium.screen.chart.ChartOptions;
import net.ecoporium.ecoporium.screen.chart.TickerChartFactory;

import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.List;

public class TickerScreenUpdateTask extends RepeatingTask {

    /**
     * Stock ticker screen
     */
    private final TickerScreen screen;

    /**
     * Ticker chart factory
     */
    private final TickerChartFactory tickerChartFactory;

    /**
     * Repeating task
     *
     * @param plugin plugin
     * @param screen screen
     */
    public TickerScreenUpdateTask(EcoporiumPlugin plugin, TickerScreen screen) {
        // every 5 seconds
        // this only updates with the data received- it does NOT fetch new data
        super(plugin, 100L, true);
        this.screen = screen;
        this.tickerChartFactory = new TickerChartFactory();
    }

    @Override
    public void run() {
        // fetch ticker
        StockTicker ticker = this.screen.fetch();

        // calculate options

        // this means that there are going to be x maps
        int rendererSize = this.screen.getImageRendererList().size();

        // number of maps (square -> side)
        int numberOfMapsPerSide = (int) Math.sqrt(rendererSize);
        // since we know it's a square, let's get the square root to find the height & width
        // each side will be * 128 (128x128 per map)
        int sideSize = numberOfMapsPerSide * 128;

        // create chart
        BufferedImage chartImage = this.tickerChartFactory.create(new Pair<>(ticker, new ChartOptions(sideSize, sideSize)));
        // split image into workable maps
        List<BufferedImage> images = ImageTools.divideIntoMapSizedParts(chartImage, false);

        // create image iterator
        Iterator<BufferedImage> bufferedImageIterator = images.listIterator();

        // get renderers and update the images
        this.screen.getImageRendererList().forEach(renderer -> {
            if (bufferedImageIterator.hasNext()) {
                renderer.setImage(bufferedImageIterator.next());
            }
        });
    }
}
