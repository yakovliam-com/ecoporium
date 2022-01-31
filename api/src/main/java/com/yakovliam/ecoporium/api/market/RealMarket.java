package com.yakovliam.ecoporium.api.market;

import com.yakovliam.ecoporium.api.market.stock.real.RealStockTicker;

import java.util.Map;

public abstract class RealMarket extends Market<RealStockTicker>{

    /**
     * Market
     *
     * @param handle handle
     */
    public RealMarket(String handle, Map<String, RealStockTicker> tickerCache) {
        super(handle, MarketType.REAL, tickerCache);
    }
}