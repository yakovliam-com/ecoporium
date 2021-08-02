package net.ecoporium.ecoporium.model.market;

import java.util.HashMap;
import java.util.Map;
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
    private final Map<String, StockTicker> tickerCache;

    /**
     * Market
     *
     * @param handle           handle
     * @param whitelistOptions whitelist options
     */
    protected Market(String handle, MarketWhitelistOptions whitelistOptions) {
        this.handle = handle;
        this.whitelistOptions = whitelistOptions;
        this.tickerCache = new HashMap<>();
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
    public Map<String, StockTicker> getTickerCache() {
        return tickerCache;
    }

    /**
     * Returns a ticker for a symbol
     *
     * @param symbol symbol
     * @return ticker
     */
    public StockTicker getTicker(String symbol) {
        // if that symbol is in the whitelist
        if (whitelistOptions.getTickers().stream()
                .noneMatch(s -> s.equalsIgnoreCase(symbol))) {
            // not allowed, so return null
            return null;
        }

        boolean contains = tickerCache.containsKey(symbol);
        StockTicker ticker = tickerCache.getOrDefault(symbol, new StockTicker(symbol));

        if (!contains) {
            tickerCache.put(symbol, ticker);
        }

        return ticker;
    }
}
