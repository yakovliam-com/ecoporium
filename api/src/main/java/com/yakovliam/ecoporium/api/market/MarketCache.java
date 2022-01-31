package com.yakovliam.ecoporium.api.market;

import com.github.benmanes.caffeine.cache.AsyncLoadingCache;
import com.yakovliam.ecoporium.api.model.cache.AsyncCache;

public abstract class MarketCache extends AsyncCache<String, Market<?>> {

    /**
     * Cache
     *
     * @param cache cache
     */
    public MarketCache(AsyncLoadingCache<String, Market<?>> cache) {
        super(cache);
    }

    /**
     * If a stock exists in any market
     *
     * @param symbol symbol
     * @return if it exists
     */
    public abstract boolean existsInAnyMarket(String symbol);
}
