package net.ecoporium.ecoporium.market;

import net.ecoporium.ecoporium.market.stock.FakeStockTicker;

import java.util.Map;

public class FakeMarket extends Market<FakeStockTicker> {

    /**
     * Market
     *
     * @param handle           handle
     * @param whitelistOptions whitelist options
     * @param tickers          tickers
     */
    public FakeMarket(String handle, MarketWhitelistOptions whitelistOptions, Map<String, FakeStockTicker> tickers) {
        super(handle, whitelistOptions, MarketType.FAKE, tickers);
    }
}
