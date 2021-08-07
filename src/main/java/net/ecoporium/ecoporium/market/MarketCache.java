package net.ecoporium.ecoporium.market;

import com.github.benmanes.caffeine.cache.Caffeine;
import net.ecoporium.ecoporium.EcoporiumPlugin;
import net.ecoporium.ecoporium.model.cache.AsyncCache;
import net.ecoporium.ecoporium.model.cache.ManualCache;

import java.util.HashMap;

public class MarketCache extends AsyncCache<String, Market> {

    /**
     * Cache
     *
     * @param plugin plugin
     */
    public MarketCache(EcoporiumPlugin plugin) {
        super(Caffeine.newBuilder()
                .buildAsync(handle -> plugin.getStorage().loadMarket(handle)));
    }
}
