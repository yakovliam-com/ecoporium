package net.ecoporium.ecoporium.screen.loader;

import net.ecoporium.ecoporium.EcoporiumPlugin;
import net.ecoporium.ecoporium.screen.TickerScreen;
import net.ecoporium.ecoporium.screen.TickerScreenManager;

import java.util.List;

public class TickerScreenLoader {

    /**
     * Ecoporium plugin
     */
    private final EcoporiumPlugin plugin;

    /**
     * Ticker screen manager
     */
    private final TickerScreenManager tickerScreenManager;

    /**
     * Ticker screen loader
     *
     * @param plugin plugin
     */
    public TickerScreenLoader(EcoporiumPlugin plugin, TickerScreenManager tickerScreenManager) {
        this.plugin = plugin;
        this.tickerScreenManager = tickerScreenManager;
    }

    /**
     * Load from storage
     */
    public void load() {
        List<TickerScreen> tickerScreenList = plugin.getStorage().loadTickerScreens();

        // load all
        tickerScreenList.forEach(tickerScreenManager::loadScreen);
    }
}
