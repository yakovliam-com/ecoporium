package net.ecoporium.ecoporium;

import net.ecoporium.ecoporium.api.Plugin;
import net.ecoporium.ecoporium.api.message.Message;
import net.ecoporium.ecoporium.command.CommandManager;
import net.ecoporium.ecoporium.config.EcoporiumConfig;
import net.ecoporium.ecoporium.market.MarketCache;
import net.ecoporium.ecoporium.task.MarketUpdater;

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
     * Market updater
     */
    private MarketUpdater marketUpdater;

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

        new CommandManager(this);

        this.marketUpdater = new MarketUpdater(this);
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
}
