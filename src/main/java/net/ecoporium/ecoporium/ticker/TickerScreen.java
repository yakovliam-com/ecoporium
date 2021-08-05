package net.ecoporium.ecoporium.ticker;

import com.github.johnnyjayjay.spigotmaps.rendering.AbstractMapRenderer;
import com.github.johnnyjayjay.spigotmaps.rendering.ImageRenderer;
import com.github.johnnyjayjay.spigotmaps.util.ImageTools;
import net.ecoporium.ecoporium.model.market.StockTicker;
import net.ecoporium.ecoporium.ticker.fetch.StockTickerFetcher;
import net.ecoporium.ecoporium.ticker.info.ScreenInfo;
import net.ecoporium.ecoporium.util.ScreenPositionUtil;

import java.awt.*;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class TickerScreen {

    /**
     * The screen's id
     */
    protected final UUID id;

    /**
     * Fetcher
     */
    protected final StockTickerFetcher fetcher;

    /**
     * A list of all of the map renderers
     */
    protected final List<ImageRenderer> imageRendererList;

    /**
     * Stock ticker screen
     *
     * @param id         id
     * @param fetcher    fetcher
     * @param screenInfo screenInfo
     */
    protected TickerScreen(UUID id, StockTickerFetcher fetcher, ScreenInfo screenInfo) {
        this.id = id;
        this.fetcher = fetcher;

        // calculate size
        int numberOfRenderersAndMaps = ScreenPositionUtil.calculateNumberOfMaps(screenInfo);

        // loop through and create renderer list
        this.imageRendererList = IntStream.range(0, numberOfRenderersAndMaps)
                .mapToObj(i -> ImageRenderer.builder()
                        .renderOnce(false)
                        .image(ImageTools.createSingleColoredImage(Color.PINK))
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * Fetches stock data
     */
    public abstract StockTicker fetch();

    /**
     * Returns the renderer list
     *
     * @return renderer list
     */
    public List<ImageRenderer> getImageRendererList() {
        return imageRendererList;
    }

    /**
     * Stops the screen from rendering
     */
    public void stopRendering() {
        // stops the screen from rendering
        this.imageRendererList.forEach(AbstractMapRenderer::stopRendering);
    }
}
