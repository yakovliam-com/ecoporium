package com.yakovliam.ecoporium.api.market;

import com.yakovliam.ecoporium.api.market.stock.fake.FakeStockTicker;

import java.util.Map;

public abstract class FakeMarket extends Market<FakeStockTicker> {

    /**
     * Market
     *
     * @param handle      handle
     * @param marketType  type
     * @param tickerCache ticker cache
     */
    protected FakeMarket(String handle, MarketType marketType, Map<String, FakeStockTicker> tickerCache) {
        super(handle, marketType, tickerCache);
    }
}
