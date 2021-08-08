package net.ecoporium.ecoporium.market;

import net.ecoporium.ecoporium.market.stock.RealStockTicker;

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
