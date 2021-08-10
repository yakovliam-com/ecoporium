package net.ecoporium.ecoporium.screen;

import net.ecoporium.ecoporium.EcoporiumPlugin;
import net.ecoporium.ecoporium.model.manager.Manager;
import net.ecoporium.ecoporium.screen.loader.TrendScreenLoader;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TrendScreenManager implements Manager {

    /**
     * Ecoporium plugin
     */
    private final EcoporiumPlugin plugin;

    /**
     * Trend screen map
     */
    private final Map<UUID, TrendScreen> trendScreenMap;

    /**
     * Trend screen manager
     *
     * @param plugin plugin
     */
    public TrendScreenManager(EcoporiumPlugin plugin) {
        this.plugin = plugin;
        this.trendScreenMap = new HashMap<>();

        // load from storage
        new TrendScreenLoader(plugin, this).load();
    }

    /**
     * Adds a trend screen to the map
     *
     * @param trendScreen trend screen
     */
    public void addTrendScreen(TrendScreen trendScreen) {
        this.trendScreenMap.put(trendScreen.getUuid(), trendScreen);
    }

    /**
     * Removes a trend screen fom the map
     *
     * @param trendScreen trend screen
     */
    public void removeTrendScreen(TrendScreen trendScreen) {
        this.trendScreenMap.remove(trendScreen.getUuid());
    }

    /**
     * Returns the trend screen map
     *
     * @return trend screen map
     */
    public Map<UUID, TrendScreen> getTrendScreenMap() {
        return trendScreenMap;
    }

    /**
     * Returns a trend screen given an id
     *
     * @param uuid uuid/id
     * @return trend screen
     */
    public TrendScreen getByUUID(UUID uuid) {
        return this.trendScreenMap.getOrDefault(uuid, null);
    }
}
