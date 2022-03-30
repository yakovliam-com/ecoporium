package com.yakovliam.ecoporium.api.market;

import com.yakovliam.ecoporium.api.market.stock.fake.FakeStockTicker;

import java.util.Map;

public abstract class FakeMarket extends Market<FakeStockTicker> {

    /**
     * Market
     *  @param handle      handle
     * @param tickerCache ticker cache
     */
    protected FakeMarket(String handle, Map<String, FakeStockTicker> tickerCache) {
        super(handle, MarketType.FAKE, tickerCache);
    }
}
