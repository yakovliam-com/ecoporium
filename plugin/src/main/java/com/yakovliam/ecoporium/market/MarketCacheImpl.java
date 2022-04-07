package com.yakovliam.ecoporium.market;

import com.yakovliam.ecoporium.EcoporiumPlugin;
import com.yakovliam.ecoporium.api.market.MarketCache;
import com.yakovliam.ecoporium.api.market.MarketType;

public class MarketCacheImpl extends MarketCache {

    /**
     * Cache
     *
     * @param plugin plugin
     */
    public MarketCacheImpl(EcoporiumPlugin plugin) {
        super(handle -> plugin.storage().loadMarket(handle));
    }

    /**
     * If a stock exists in any market
     *
     * @param symbol symbol
     * @return if it exists
     */
    @Override
    public boolean existsInAnyMarket(String symbol) {
        return this.cache().synchronous().asMap().values().stream()
                .anyMatch(m -> {
                    if (m.marketType() == MarketType.FAKE) {
                        return ((FakeMarketImpl) m).tickerCache().containsKey(symbol);
                    } else if (m.marketType() == MarketType.REAL) {
                        return ((RealMarketImpl) m).tickerCache().containsKey(symbol);
                    } else {
                        return false;
                    }
                });
    }
}
