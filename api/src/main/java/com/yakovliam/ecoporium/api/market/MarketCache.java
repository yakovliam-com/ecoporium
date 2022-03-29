package com.yakovliam.ecoporium.api.market;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.yakovliam.ecoporium.api.functional.ObjectiveConsumer;
import com.yakovliam.ecoporium.api.model.cache.AsyncCache;

public abstract class MarketCache extends AsyncCache<String, Market<?>> {

    /**
     * Cache
     *
     * @param consumer objective consumer
     */
    public MarketCache(ObjectiveConsumer<String, Market<?>> consumer) {
        super(Caffeine.newBuilder().buildAsync((consumer::accept)));
    }

    /**
     * If a stock exists in any market
     *
     * @param symbol symbol
     * @return if it exists
     */
    public abstract boolean existsInAnyMarket(String symbol);
}
