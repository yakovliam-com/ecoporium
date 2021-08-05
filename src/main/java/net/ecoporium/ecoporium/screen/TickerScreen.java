package net.ecoporium.ecoporium.screen;

import com.github.johnnyjayjay.spigotmaps.rendering.AbstractMapRenderer;
import com.github.johnnyjayjay.spigotmaps.rendering.ImageRenderer;
import com.github.johnnyjayjay.spigotmaps.util.ImageTools;
import net.ecoporium.ecoporium.EcoporiumPlugin;
import net.ecoporium.ecoporium.market.StockTicker;
import net.ecoporium.ecoporium.screen.data.TickerScreenMapData;
import net.ecoporium.ecoporium.screen.fetch.StockTickerFetcher;
import net.ecoporium.ecoporium.screen.data.TickerScreenInfo;
import net.ecoporium.ecoporium.util.ScreenPositionUtil;
import org.bukkit.Bukkit;
import org.bukkit.map.MapView;

import java.awt.*;
import java.util.*;
import java.util.List;
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
    protected List<ImageRenderer> imageRendererList;

    /**
     * Ticker screen map data
     */
    private TickerScreenMapData tickerScreenMapData;

    /**
     * Ticker screen info
     */
    private final TickerScreenInfo tickerScreenInfo;

    /**
     * Stock ticker screen
     *
     * @param id               id
     * @param fetcher          fetcher
     * @param tickerScreenInfo tickerscreen info
     */
    protected TickerScreen(UUID id, StockTickerFetcher fetcher, TickerScreenInfo tickerScreenInfo) {
        this.id = id;
        this.fetcher = fetcher;
        this.tickerScreenInfo = tickerScreenInfo;

        // calculate size
        int numberOfRenderersAndMaps = ScreenPositionUtil.calculateNumberOfMaps(tickerScreenInfo);

        // loop through and create renderer list
        this.imageRendererList = IntStream.range(0, numberOfRenderersAndMaps)
                .mapToObj(i -> ImageRenderer.builder()
                        .renderOnce(false)
                        .image(ImageTools.createSingleColoredImage(Color.BLACK))
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * Fetches stock data
     */
    public abstract StockTicker fetch();

    /**
     * Cancels tasks
     */
    public abstract void cancel();

    /**
     * Starts tasks
     *
     * @param plugin applicable plugin context
     */
    public abstract void start(EcoporiumPlugin plugin);

    /**
     * Returns the renderer list
     *
     * @return renderer list
     */
    public List<ImageRenderer> getImageRendererList() {
        return imageRendererList;
    }

    /**
     * Stops the screen from rendering and doing tasks
     */
    public void stop() {
        // cancel
        this.cancel();
        // stops the screen from rendering
        this.imageRendererList.forEach(AbstractMapRenderer::stopRendering);
    }

    /**
     * Returns the uuid of the screen
     *
     * @return uuid
     */
    public UUID getId() {
        return id;
    }

    /**
     * Returns the screen map data
     *
     * @return screen map data
     */
    public TickerScreenMapData getTickerScreenMapData() {
        return tickerScreenMapData;
    }

    /**
     * Set ticker screen map data
     *
     * @param tickerScreenMapData ticker screen map data
     */
    public void setTickerScreenMapData(TickerScreenMapData tickerScreenMapData) {
        this.tickerScreenMapData = tickerScreenMapData;
    }

    /**
     * Ticker screen info
     *
     * @return info
     */
    public TickerScreenInfo getTickerScreenInfo() {
        return tickerScreenInfo;
    }

    /**
     * Initializes the renderers onto precalculated maps stored in the {@code TickerScreenMapData} object
     */
    public void initializePreCalculatedMaps() {
        if (tickerScreenMapData == null) {
            return;
        }

        Iterator<ImageRenderer> imageRendererIterator = this.imageRendererList.iterator();

        for (Integer mapId : this.tickerScreenMapData.getMapIds()) {
            MapView mapView = Bukkit.getMap(mapId);

            if (mapView == null) {
                continue;
            }

            // apply renderer
            ImageRenderer renderer = imageRendererIterator.next();
            mapView.addRenderer(renderer);

        }
    }
}
