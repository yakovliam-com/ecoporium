package com.yakovliam.ecoporium.api.market;

import com.yakovliam.ecoporium.api.market.stock.StockTicker;

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
     * @param handle      handle
     * @param marketType  type
     * @param tickerCache ticker cache
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
     * If the market contains a stock
     *
     * @param symbolOrAlias stock
     * @return contains
     */
    public boolean containsStock(String symbolOrAlias) {
        return tickerCache.values().stream()
                .anyMatch(t -> t.getSymbol().equalsIgnoreCase(symbolOrAlias) || t.getAliases().stream()
                        .anyMatch(a -> a.equalsIgnoreCase(symbolOrAlias)));
    }

    /**
     * Returns a stock by symbol or alias
     *
     * @param symbolOrAlias symbol or alias
     * @return symbol or alias
     */
    public T getStock(String symbolOrAlias) {
        return tickerCache.values().stream()
                .filter(t -> t.getSymbol().equalsIgnoreCase(symbolOrAlias) || t.getAliases().stream()
                        .anyMatch(a -> a.equalsIgnoreCase(symbolOrAlias)))
                .findFirst()
                .orElse(null);
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
