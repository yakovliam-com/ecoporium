package net.ecoporium.ecoporium.screen;

import net.ecoporium.ecoporium.market.stock.StockTicker;

public class TrendScreen {

    /**
     * The associated ticker
     */
    private final StockTicker<?> ticker;

    public TrendScreen(StockTicker<?> ticker) {
        this.ticker = ticker;
    }
}
