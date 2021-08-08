package net.ecoporium.ecoporium.market;

import net.ecoporium.ecoporium.market.stock.FakeStockTicker;

import java.util.Map;

public class FakeMarket extends Market<FakeStockTicker> {

    /**
     * Market
     *
     * @param handle           handle
     * @param tickers          tickers
     */
    public FakeMarket(String handle, Map<String, FakeStockTicker> tickers) {
        super(handle, MarketType.FAKE, tickers);
    }
}
