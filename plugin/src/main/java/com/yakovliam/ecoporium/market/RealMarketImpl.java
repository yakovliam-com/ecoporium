package com.yakovliam.ecoporium.market;

import com.yakovliam.ecoporium.api.market.RealMarket;
import com.yakovliam.ecoporium.api.market.stock.real.RealStockTicker;

import java.util.Map;

public class RealMarketImpl extends RealMarket {

    /**
     * Market
     *
     * @param handle      handle
     * @param tickerCache cache
     */
    public RealMarketImpl(String handle, Map<String, RealStockTicker> tickerCache) {
        super(handle, tickerCache);
    }
}
