package net.ecoporium.ecoporium.ticker;

import net.ecoporium.ecoporium.model.manager.Manager;

import java.util.Map;
import java.util.UUID;

public class TickerScreenManager implements Manager {

    /**
     * Screen map
     * [Random ID, Ticker Screen Object]
     */
    private final Map<UUID, TickerScreen> screenMap;

    /**
     * Ticker screen manager
     *
     * @param screenMap screenMap
     */
    public TickerScreenManager(Map<UUID, TickerScreen> screenMap) {
        this.screenMap = screenMap;

        // load from storage
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
     * Removes a ticker screen
     *
     * @param id id
     */
    public void removeScreen(UUID id) {
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
