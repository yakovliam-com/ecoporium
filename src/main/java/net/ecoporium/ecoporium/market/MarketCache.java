package net.ecoporium.ecoporium.market;

import net.ecoporium.ecoporium.model.cache.ManualCache;
import net.ecoporium.ecoporium.model.market.Market;

import java.util.HashMap;

public class MarketCache extends ManualCache<String, Market> {

    /**
     * Cache
     */
    protected MarketCache() {
        super(new HashMap<>());
    }
}
