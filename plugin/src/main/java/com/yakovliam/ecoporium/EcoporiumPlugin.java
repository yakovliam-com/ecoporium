package com.yakovliam.ecoporium;

import com.yakovliam.ecoporium.api.message.Message;
import com.yakovliam.ecoporium.command.CommandManager;
import com.yakovliam.ecoporium.config.EcoporiumConfig;
import com.yakovliam.ecoporium.expansion.EcoporiumExpansion;
import com.yakovliam.ecoporium.listener.PlayerListener;
import com.yakovliam.ecoporium.market.MarketCacheImpl;
import com.yakovliam.ecoporium.message.Messages;
import com.yakovliam.ecoporium.storage.Storage;
import com.yakovliam.ecoporium.storage.implementation.json.JsonStorageImplementation;
import com.yakovliam.ecoporium.task.MarketUpdateTask;
import com.yakovliam.ecoporium.user.UserCacheImpl;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;

public class EcoporiumPlugin extends com.yakovliam.ecoporium.api.EcoporiumPlugin {

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
    private MarketCacheImpl marketCacheImpl;

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
    private UserCacheImpl userCacheImpl;

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

        // register self in service provider
        this.getServer().getServicesManager().register(com.yakovliam.ecoporium.api.EcoporiumPlugin.class, this, this, ServicePriority.Normal);

        // initialize audience provider
        Message.initAudience(this);

        this.ecoporiumConfig = new EcoporiumConfig(this, provideConfigAdapter("config.yml"));
        this.langConfig = new EcoporiumConfig(this, provideConfigAdapter("lang.yml"));
        this.marketCacheImpl = new MarketCacheImpl(this);

        this.storage = new Storage(new JsonStorageImplementation(this));

        this.userCacheImpl = new UserCacheImpl(this);

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
        this.getUserCache().getCache().synchronous().asMap().values().forEach(user -> getStorage().saveUser(user, false));
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
    @Override
    public MarketCacheImpl getMarketCache() {
        return marketCacheImpl;
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
    @Override
    public UserCacheImpl getUserCache() {
        return userCacheImpl;
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
        if (this.getServer().getPluginManager().getPlugin("Vault") == null) {
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
