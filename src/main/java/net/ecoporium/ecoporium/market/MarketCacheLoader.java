package net.ecoporium.ecoporium.market;

import net.ecoporium.ecoporium.EcoporiumPlugin;
import net.ecoporium.ecoporium.api.config.generic.adapter.ConfigurationAdapter;
import net.ecoporium.ecoporium.api.wrapper.Pair;
import net.ecoporium.ecoporium.builder.market.MarketBuilder;
import net.ecoporium.ecoporium.model.cache.loader.CacheLoader;

import static net.ecoporium.ecoporium.config.EcoporiumConfigKeys.MARKET_HANDLES_PATHS;

public class MarketCacheLoader extends CacheLoader<MarketCache> {

    /**
     * Ecoporium plugin
     */
    private final EcoporiumPlugin plugin;

    /**
     * Market builder
     */
    private final MarketBuilder marketBuilder;

    /**
     * Cache
     *
     * @param cache cache
     */
    public MarketCacheLoader(EcoporiumPlugin plugin, MarketCache cache) {
        super(cache);
        this.plugin = plugin;
        this.marketBuilder = new MarketBuilder();
    }

    @Override
    public void load() {
        // get configuration
        ConfigurationAdapter adapter = plugin.getEcoporiumConfig().getAdapter();

        MARKET_HANDLES_PATHS.get(adapter).forEach((handle, path) -> cache.getMap().put(handle, marketBuilder.build(new Pair<>(handle, path), adapter)));
    }
}
