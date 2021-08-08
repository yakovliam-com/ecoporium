package net.ecoporium.ecoporium.market;

import net.ecoporium.ecoporium.market.stock.StockTicker;

import java.util.Map;

public abstract class Market<T extends StockTicker<?>> {

    /**
     * The given name of the market
     * <p>
     * This is configurable and may look something like
     * "Stock Market" or "Crypto"
     * <p>
     * The associated value is up to the server owner
     */
    private final String handle;

    /**
     * Market type
     */
    private final MarketType marketType;

    /**
     *
     */
    private final Map<String, T> tickerCache;

    /**
     * Market
     *
     * @param handle           handle
     * @param marketType       type
     * @param tickerCache      ticker cache
     */
    protected Market(String handle, MarketType marketType, Map<String, T> tickerCache) {
        this.handle = handle;
        this.marketType = marketType;
        this.tickerCache = tickerCache;
    }

    /**
     * Returns a ticker cache
     *
     * @return ticker cache
     */
    public Map<String, T> getTickerCache() {
        return tickerCache;
    }

    /**
     * Returns the handle
     *
     * @return handle
     */
    public String getHandle() {
        return handle;
    }

    /**
     * Returns the market type
     *
     * @return type
     */
    public MarketType getMarketType() {
        return marketType;
    }
}
