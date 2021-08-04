package net.ecoporium.ecoporium.ticker;

import net.ecoporium.ecoporium.model.market.StockTicker;
import net.ecoporium.ecoporium.ticker.fetch.StockTickerFetcher;
import net.ecoporium.ecoporium.ticker.info.ScreenPositionalInfo;

import java.util.UUID;

public class DynamicTickerScreen extends TickerScreen {

    /**
     * Stock ticker screen
     *
     * @param id             id
     * @param fetcher        fetcher
     * @param positionalInfo positional info
     */
    protected DynamicTickerScreen(UUID id, StockTickerFetcher fetcher, ScreenPositionalInfo positionalInfo) {
        super(id, fetcher, positionalInfo);
    }

    @Override
    public StockTicker fetch() {
        return null;
    }
}
