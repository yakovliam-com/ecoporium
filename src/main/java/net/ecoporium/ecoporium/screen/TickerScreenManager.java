package net.ecoporium.ecoporium.screen;

import net.ecoporium.ecoporium.EcoporiumPlugin;
import net.ecoporium.ecoporium.model.manager.Manager;
import net.ecoporium.ecoporium.screen.loader.TickerScreenLoader;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TickerScreenManager implements Manager {

    /**
     * Screen map
     * [Random ID, Ticker Screen Object]
     */
    private final Map<UUID, TickerScreen> screenMap;

    /**
     * Ecoporium plugin
     */
    private final EcoporiumPlugin plugin;

    /**
     * Ticker screen manager
     *
     * @param plugin plugin
     */
    public TickerScreenManager(EcoporiumPlugin plugin) {
        this.plugin = plugin;
        this.screenMap = new HashMap<>();

        // load from storage
        new TickerScreenLoader(plugin, this).load();
    }

    /**
     * Adds a new screen
     * <p>
     * Doesn't handle storage, that has to be handled another way
     *
     * @param id     id
     * @param screen screen
     */
    public void addScreen(UUID id, TickerScreen screen) {
        this.screenMap.put(id, screen);
    }

    /**
     * Loads a screen into memory and starts rendering
     *
     * @param screen screen
     */
    public void loadScreen(TickerScreen screen) {
        addScreen(screen.getId(), screen);
        // initialize maps
        screen.initializePreCalculatedMaps();
        // start screen
        screen.start(plugin);
    }

    /**
     * Removes a ticker screen
     *
     * @param id id
     */
    public void removeScreen(UUID id) {
        // get screen
        TickerScreen screen = this.screenMap.get(id);

        // stop screen

        screen.stopScreen();

        // remove
        this.screenMap.remove(id);
    }

    /**
     * Returns a random screen that uses a specific symbol
     *
     * @param symbol symbol
     * @return random screen
     */
    public TickerScreen getScreenBySymbol(String symbol) {
        return this.screenMap.values().stream()
                .filter(s -> s instanceof StaticTickerScreen)
                .map(s -> (StaticTickerScreen) s)
                .filter(s -> s.getSymbol().equalsIgnoreCase(symbol))
                .findFirst()
                .orElse(null);
    }

    /**
     * Returns a ticker screen by id
     *
     * @param id id
     * @return screen
     */
    public TickerScreen get(UUID id) {
        return this.screenMap.getOrDefault(id, null);
    }
}
