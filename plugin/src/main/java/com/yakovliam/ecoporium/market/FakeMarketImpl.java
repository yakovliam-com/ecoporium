package com.yakovliam.ecoporium.market;

import com.yakovliam.ecoporium.api.market.FakeMarket;
import com.yakovliam.ecoporium.api.market.stock.fake.FakeStockTicker;

import java.util.Map;

public class FakeMarketImpl extends FakeMarket {

    /**
     * Market
     *
     * @param handle  handle
     * @param tickers tickers
     */
    public FakeMarketImpl(String handle, Map<String, FakeStockTicker> tickers) {
        super(handle, tickers);
    }
}
