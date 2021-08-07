package net.ecoporium.ecoporium.market;

import net.ecoporium.ecoporium.market.stock.RealStockTicker;

import java.util.Map;

public class RealMarket extends Market<RealStockTicker> {

    /**
     * Market
     *
     * @param handle           handle
     * @param whitelistOptions whitelist options
     */
    public RealMarket(String handle, MarketWhitelistOptions whitelistOptions, Map<String, RealStockTicker> tickerCache) {
        super(handle, whitelistOptions, MarketType.REAL, tickerCache);
    }
}
