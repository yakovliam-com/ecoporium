package net.ecoporium.ecoporium;

import net.ecoporium.ecoporium.api.Plugin;
import net.ecoporium.ecoporium.api.message.Message;
import net.ecoporium.ecoporium.command.CommandManager;
import net.ecoporium.ecoporium.config.EcoporiumConfig;
import net.ecoporium.ecoporium.market.MarketCache;
import net.ecoporium.ecoporium.message.Messages;
import net.ecoporium.ecoporium.session.screen.TickerScreenCreatorSessionManager;
import net.ecoporium.ecoporium.storage.Storage;
import net.ecoporium.ecoporium.storage.implementation.json.JsonStorageImplementation;
import net.ecoporium.ecoporium.task.MarketUpdater;

import java.util.HashMap;

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
     * Ticker screen session manager
     */
    private TickerScreenCreatorSessionManager tickerScreenCreatorSessionManager;

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

        this.tickerScreenCreatorSessionManager = new TickerScreenCreatorSessionManager(this);

        new CommandManager(this);
        new MarketUpdater(this);

        this.messages = new Messages();
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
     * Returns the ticker screen session manager
     *
     * @return session manager
     */
    public TickerScreenCreatorSessionManager getTickerScreenCreatorSessionManager() {
        return tickerScreenCreatorSessionManager;
    }
}
