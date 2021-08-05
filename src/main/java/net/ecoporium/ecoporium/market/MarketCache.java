package net.ecoporium.ecoporium.market;

import net.ecoporium.ecoporium.EcoporiumPlugin;
import net.ecoporium.ecoporium.model.cache.ManualCache;

import java.util.HashMap;

public class MarketCache extends ManualCache<String, Market> {

    /**
     * Cache
     *
     * @param plugin plugin
     */
    public MarketCache(EcoporiumPlugin plugin) {
        super(new HashMap<>());

        // load
        new MarketCacheLoader(plugin, this).load();
    }
}
