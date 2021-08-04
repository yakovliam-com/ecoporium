package net.ecoporium.ecoporium.ticker;

import com.github.johnnyjayjay.spigotmaps.rendering.AbstractMapRenderer;
import com.github.johnnyjayjay.spigotmaps.rendering.ImageRenderer;
import net.ecoporium.ecoporium.model.market.StockTicker;
import net.ecoporium.ecoporium.ticker.fetch.StockTickerFetcher;
import net.ecoporium.ecoporium.ticker.info.ScreenPositionalInfo;

import java.util.List;
import java.util.UUID;

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
     * Positional info
     */
    protected final ScreenPositionalInfo screenPositionalInfo;

    /**
     * Stock ticker screen
     *
     * @param id             id
     * @param fetcher        fetcher
     * @param positionalInfo positional info
     */
    protected TickerScreen(UUID id, StockTickerFetcher fetcher, ScreenPositionalInfo positionalInfo) {
        this.id = id;
        this.fetcher = fetcher;
        this.screenPositionalInfo = positionalInfo;

        // TODO calculate size using positional info & make image renderer list based on that (default empty color)
        this.imageRendererList = null;
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
