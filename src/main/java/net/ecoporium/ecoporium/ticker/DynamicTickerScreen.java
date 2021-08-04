package net.ecoporium.ecoporium.ticker;

import net.ecoporium.ecoporium.model.market.StockTicker;
import net.ecoporium.ecoporium.ticker.fetch.StockTickerFetcher;
import net.ecoporium.ecoporium.ticker.info.ScreenInfo;

import java.util.UUID;

public class DynamicTickerScreen extends TickerScreen {
    /**
     * Stock ticker screen
     *
     * @param id         id
     * @param fetcher    fetcher
     * @param screenInfo screenInfo
     */
    protected DynamicTickerScreen(UUID id, StockTickerFetcher fetcher, ScreenInfo screenInfo) {
        super(id, fetcher, screenInfo);
    }

    /**
     * Fetches stock data
     */
    @Override
    public StockTicker fetch() {
        return null;
    }
}
