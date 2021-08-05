package net.ecoporium.ecoporium.screen;

import net.ecoporium.ecoporium.EcoporiumPlugin;
import net.ecoporium.ecoporium.model.market.StockTicker;
import net.ecoporium.ecoporium.screen.fetch.StockTickerFetcher;
import net.ecoporium.ecoporium.screen.data.TickerScreenInfo;

import java.util.UUID;

public class DynamicTickerScreen extends TickerScreen {
    /**
     * Stock ticker screen
     *
     * @param id         id
     * @param fetcher    fetcher
     * @param tickerScreenInfo screenInfo
     */
    protected DynamicTickerScreen(UUID id, StockTickerFetcher fetcher, TickerScreenInfo tickerScreenInfo) {
        super(id, fetcher, tickerScreenInfo);
    }

    /**
     * Fetches stock data
     */
    @Override
    public StockTicker fetch() {
        return null;
    }

    @Override
    public void cancel() {

    }

    @Override
    public void start(EcoporiumPlugin plugin) {

    }
}
