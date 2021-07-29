package net.ecoporium.ecoporium.model.market;

import net.ecoporium.ecoporium.model.cache.AsyncCache;
import yahoofinance.Stock;

import java.util.concurrent.CompletableFuture;

public abstract class Market {

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
     * The market's ticker cache
     * <p>
     * A cache (async) that contains all of the market's currently used tickers
     */
    private final AsyncCache<String, Stock> tickerCache;

    /**
     * Market
     *
     * @param handle           handle
     * @param whitelistOptions whitelist options
     * @param tickerCache      ticker cache
     */
    protected Market(String handle, MarketWhitelistOptions whitelistOptions, AsyncCache<String, Stock> tickerCache) {
        this.handle = handle;
        this.whitelistOptions = whitelistOptions;
        this.tickerCache = tickerCache;
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
     * Returns the ticker cache
     *
     * @return cache
     */
    public AsyncCache<String, Stock> getTickerCache() {
        return tickerCache;
    }

    /**
     * Updates a ticker by invalidating the associated symbol and re-gets the value
     *
     * @param symbol symbol
     */
    public CompletableFuture<Stock> updateTicker(String symbol) {
        invalidate(symbol);
        return getTicker(symbol);
    }

    /**
     * Returns a stock once it has gotten the values
     *
     * @param symbol symbol
     * @return stock
     */
    public CompletableFuture<Stock> getTicker(String symbol) {
        return tickerCache.getCache().get(symbol);
    }

    /**
     * Invalidates a ticker and prepares it for updating
     *
     * @param symbol symbol
     */
    protected void invalidate(String symbol) {
        tickerCache.getCache().synchronous().invalidate(symbol);
    }
}
