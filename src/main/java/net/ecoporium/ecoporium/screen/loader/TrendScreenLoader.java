package net.ecoporium.ecoporium.screen.loader;

import net.ecoporium.ecoporium.EcoporiumPlugin;
import net.ecoporium.ecoporium.model.loader.Loader;
import net.ecoporium.ecoporium.screen.TrendScreen;
import net.ecoporium.ecoporium.screen.TrendScreenManager;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TrendScreenLoader implements Loader {

    /**
     * Ecoporium plugin
     */
    private final EcoporiumPlugin plugin;

    /**
     * Trend screen manager
     */
    private final TrendScreenManager trendScreenManager;

    /**
     * Trend screen loader
     *
     * @param plugin plugin
     */
    public TrendScreenLoader(EcoporiumPlugin plugin, TrendScreenManager trendScreenManager) {
        this.plugin = plugin;
        this.trendScreenManager = trendScreenManager;
    }

    /**
     * Loads the trend screens from storage
     */
    @Override
    public void load() {
        // load from storage
        List<TrendScreen> trendScreenList = plugin.getStorage().loadTrendScreens();

        // get map
        Map<UUID, TrendScreen> trendScreenMap = trendScreenManager.getTrendScreenMap();
        // add to manager
        trendScreenList.forEach(trendScreen -> {
            trendScreenMap.put(trendScreen.getUuid(), trendScreen);
            // start rendering
            trendScreen.startScreen(plugin);
        });
    }
}
