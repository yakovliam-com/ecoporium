package net.ecoporium.ecoporium.market;

import net.ecoporium.ecoporium.market.stock.StockTicker;

import java.util.Map;

public abstract class Market<T extends StockTicker> {

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
     * The market's whitelisted ticker options
     * <p>
     * If this is {@code null}, then all stock ticker symbols are allowed
     * If it is NOT {@code null}, then only the given ticker symbols will be allowed to be traded
     */
    private final MarketWhitelistOptions whitelistOptions;

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
     * @param whitelistOptions whitelist options
     * @param marketType       type
     * @param tickerCache      ticker cache
     */
    protected Market(String handle, MarketWhitelistOptions whitelistOptions, MarketType marketType, Map<String, T> tickerCache) {
        this.handle = handle;
        this.whitelistOptions = whitelistOptions;
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
     * Returns the whitelist options
     *
     * @return whitelist options
     */
    public MarketWhitelistOptions getWhitelistOptions() {
        return whitelistOptions;
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
