package com.yakovliam.ecoporium.market;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.yakovliam.ecoporium.EcoporiumPlugin;
import com.yakovliam.ecoporium.api.model.cache.AsyncCache;

public class MarketCache extends AsyncCache<String, Market<?>> {

    /**
     * Cache
     *
     * @param plugin plugin
     */
    public MarketCache(EcoporiumPlugin plugin) {
        super(Caffeine.newBuilder()
                .buildAsync(handle -> plugin.getStorage().loadMarket(handle)));
    }

    /**
     * If a stock exists in any market
     *
     * @param symbol symbol
     * @return if it exists
     */
    public boolean existsInAnyMarket(String symbol) {
        return this.getCache().synchronous().asMap().values().stream()
                .anyMatch(m -> {
                    if (m.getMarketType() == MarketType.FAKE) {
                        return ((FakeMarket) m).getTickerCache().containsKey(symbol);
                    } else if (m.getMarketType() == MarketType.REAL) {
                        return ((RealMarket) m).getTickerCache().containsKey(symbol);
                    } else {
                        return false;
                    }
                });
    }
}
