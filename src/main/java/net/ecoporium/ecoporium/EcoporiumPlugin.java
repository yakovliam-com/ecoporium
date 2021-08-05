package net.ecoporium.ecoporium;

import net.ecoporium.ecoporium.api.Plugin;
import net.ecoporium.ecoporium.api.message.Message;
import net.ecoporium.ecoporium.command.CommandManager;
import net.ecoporium.ecoporium.config.EcoporiumConfig;
import net.ecoporium.ecoporium.map.MapPlacementHandler;
import net.ecoporium.ecoporium.market.MarketCache;
import net.ecoporium.ecoporium.message.Messages;
import net.ecoporium.ecoporium.storage.Storage;
import net.ecoporium.ecoporium.storage.implementation.json.JsonStorageImplementation;
import net.ecoporium.ecoporium.task.MarketUpdater;
import net.ecoporium.ecoporium.screen.TickerScreenManager;

public class EcoporiumPlugin extends Plugin {

    /**
     * Ecoporium configuration
     */
    private EcoporiumConfig ecoporiumConfig;

    /**
     * Market cache
     */
    private MarketCache marketCache;

    /**
     * Storage
     */
    private Storage storage;

    /**
     * Messages
     */
    private Messages messages;

    /**
     * Map placement handler
     */
    private MapPlacementHandler mapPlacementHandler;

    /**
     * Ticker screen manager
     */
    private TickerScreenManager tickerScreenManager;

    @Override
    public void onLoad() {
    }

    @Override
    public void onEnable() {
        super.onEnable();

        // initialize audience provider
        Message.initAudience(this);

        this.ecoporiumConfig = new EcoporiumConfig(this, provideConfigAdapter("config.yml"));
        this.marketCache = new MarketCache(this);

        this.storage = new Storage(new JsonStorageImplementation(this));

        new CommandManager(this);
        new MarketUpdater(this);

        this.messages = new Messages();

        this.mapPlacementHandler = new MapPlacementHandler(this);

        this.tickerScreenManager = new TickerScreenManager(this);
    }

    /**
     * Returns the Ecoporium config
     *
     * @return config
     */
    public EcoporiumConfig getEcoporiumConfig() {
        return ecoporiumConfig;
    }

    /**
     * Returns the market cache
     *
     * @return market cache
     */
    public MarketCache getMarketCache() {
        return marketCache;
    }

    /**
     * Returns storage
     *
     * @return storage
     */
    public Storage getStorage() {
        return storage;
    }

    /**
     * Returns messages
     *
     * @return messages
     */

    public Messages getMessages() {
        return messages;
    }

    /**
     * Returns map placement handler
     *
     * @return map placement handler
     */
    public MapPlacementHandler getMapPlacementHandler() {
        return mapPlacementHandler;
    }

    /**
     * Returns ticker screen manager
     *
     * @return ticker screen manager
     */
    public TickerScreenManager getTickerScreenManager() {
        return tickerScreenManager;
    }
}
