package com.yakovliam.ecoporium.market;

import com.yakovliam.ecoporium.lib.caffeine.cache.Caffeine;
import com.yakovliam.ecoporium.EcoporiumPlugin;
import com.yakovliam.ecoporium.api.market.MarketCache;
import com.yakovliam.ecoporium.api.market.MarketType;

public class MarketCacheImpl extends MarketCache {

    /**
     * Cache
     *
     * @param plugin pluging
     */
    public MarketCacheImpl(EcoporiumPlugin plugin) {
        super(Caffeine.newBuilder()
                .buildAsync(handle -> plugin.getStorage().loadMarket(handle)));
    }

    /**
     * If a stock exists in any market
     *
     * @param symbol symbol
     * @return if it exists
     */
    @Override
    public boolean existsInAnyMarket(String symbol) {
        return this.getCache().synchronous().asMap().values().stream()
                .anyMatch(m -> {
                    if (m.getMarketType() == MarketType.FAKE) {
                        return ((FakeMarketImpl) m).getTickerCache().containsKey(symbol);
                    } else if (m.getMarketType() == MarketType.REAL) {
                        return ((RealMarketImpl) m).getTickerCache().containsKey(symbol);
                    } else {
                        return false;
                    }
                });
    }
}
