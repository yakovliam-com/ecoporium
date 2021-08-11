package net.ecoporium.ecoporium.screen;

import net.ecoporium.ecoporium.EcoporiumPlugin;
import net.ecoporium.ecoporium.model.manager.Manager;
import net.ecoporium.ecoporium.screen.loader.TrendScreenLoader;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TrendScreenManager implements Manager {

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
        /**
         * Ecoporium plugin
         */
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
     * Returns a trend screen with a certain map id
     *
     * @param mapId map id
     * @return screen
     */
    public TrendScreen getByMapId(int mapId) {
        return this.trendScreenMap.values().stream()
                .filter(s -> s.getMapInfo().getMapIds().contains(mapId))
                .findFirst()
                .orElse(null);
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
