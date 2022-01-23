package net.ecoporium.ecoporium;

import net.ecoporium.ecoporium.api.Plugin;
import net.ecoporium.ecoporium.api.message.Message;
import net.ecoporium.ecoporium.command.CommandManager;
import net.ecoporium.ecoporium.config.EcoporiumConfig;
import net.ecoporium.ecoporium.expansion.EcoporiumExpansion;
import net.ecoporium.ecoporium.listener.PlayerListener;
import net.ecoporium.ecoporium.market.MarketCache;
import net.ecoporium.ecoporium.message.Messages;
import net.ecoporium.ecoporium.storage.Storage;
import net.ecoporium.ecoporium.storage.implementation.json.JsonStorageImplementation;
import net.ecoporium.ecoporium.task.MarketUpdateTask;
import net.ecoporium.ecoporium.user.UserCache;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;

public class EcoporiumPlugin extends Plugin {

    /**
     * Ecoporium configuration
     */
    private EcoporiumConfig ecoporiumConfig;

    /**
     * Language config
     */
    private EcoporiumConfig langConfig;

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
     * User cache
     */
    private UserCache userCache;

    /**
     * Economy
     */
    private Economy economy;

    @Override
    public void onLoad() {
    }

    @Override
    public void onEnable() {
        super.onEnable();

        // initialize audience provider
        Message.initAudience(this);

        this.ecoporiumConfig = new EcoporiumConfig(this, provideConfigAdapter("config.yml"));
        this.langConfig = new EcoporiumConfig(this, provideConfigAdapter("lang.yml"));
        this.marketCache = new MarketCache(this);

        this.storage = new Storage(new JsonStorageImplementation(this));

        this.userCache = new UserCache(this);

        new CommandManager(this);

        loadMessages();

        this.getServer().getPluginManager().registerEvents(new PlayerListener(this), this);

        // setup economy
        if (!setupEconomy()) {
            getLogger().severe("Not able to find an Economy provider!");
        }

        // expansion
        new EcoporiumExpansion(this).register();

        // create market update task
        new MarketUpdateTask(this).start();
    }

    @Override
    public void onDisable() {
        // save users
        getUserCache().getCache().synchronous().asMap().values().forEach(user -> getStorage().saveUser(user, false));
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
     * Returns user cache
     *
     * @return user cache
     */
    public UserCache getUserCache() {
        return userCache;
    }

    /**
     * Returns economy
     *
     * @return economy
     */
    public Economy getEconomy() {
        return economy;
    }

    /**
     * Returns lang config
     *
     * @return lang config
     */
    public EcoporiumConfig getLangConfig() {
        return langConfig;
    }

    /**
     * Loads messages
     */
    public void loadMessages() {
        this.messages = new Messages(this);
    }

    /**
     * Sets up economy
     *
     * @return economy
     */
    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }

        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }

        this.economy = rsp.getProvider();

        return true;
    }
}
