package com.yakovliam.ecoporium.market;

import com.yakovliam.ecoporium.market.stock.real.RealStockTicker;

import java.util.Map;

public class RealMarket extends Market<RealStockTicker> {

    /**
     * Market
     *
     * @param handle           handle
     */
    public RealMarket(String handle, Map<String, RealStockTicker> tickerCache) {
        super(handle, MarketType.REAL, tickerCache);
    }
}
