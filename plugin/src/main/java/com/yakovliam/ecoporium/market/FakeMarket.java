package com.yakovliam.ecoporium.market;

import com.yakovliam.ecoporium.market.stock.fake.FakeStockTicker;

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
