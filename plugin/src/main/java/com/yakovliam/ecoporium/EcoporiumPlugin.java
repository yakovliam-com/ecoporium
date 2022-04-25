package com.yakovliam.ecoporium;

import com.yakovliam.ecoporium.command.CommandManager;
import com.yakovliam.ecoporium.config.ConfigSupervisor;
import com.yakovliam.ecoporium.expansion.EcoporiumExpansion;
import com.yakovliam.ecoporium.listener.PlayerListener;
import com.yakovliam.ecoporium.market.MarketCacheImpl;
import com.yakovliam.ecoporium.storage.Storage;
import com.yakovliam.ecoporium.storage.implementation.json.JsonStorageImplementation;
import com.yakovliam.ecoporium.task.MarketUpdateTask;
import com.yakovliam.ecoporium.user.UserCacheImpl;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;

public class EcoporiumPlugin extends com.yakovliam.ecoporium.api.EcoporiumPlugin {

    private static BukkitAudiences audiences;

    /**
     * Market cache
     */
    private MarketCacheImpl marketCacheImpl;

    /**
     * Storage
     */
    private Storage storage;

    /**
     * Config supervisor
     */
    private ConfigSupervisor configSupervisor;

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
        audiences = BukkitAudiences.create(this);

        // load configurations (messages, etc.)
        this.configSupervisor = new ConfigSupervisor(this);

        this.marketCacheImpl = new MarketCacheImpl(this);

        this.storage = new Storage(new JsonStorageImplementation(this));

        this.userCacheImpl = new UserCacheImpl(this);

        new CommandManager(this);

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
        this.userCache().cache().synchronous().asMap().values().forEach(user -> storage().saveUser(user, false));
    }

    /**
     * Returns the market cache
     *
     * @return market cache
     */
    @Override
    public MarketCacheImpl marketCache() {
        return marketCacheImpl;
    }

    /**
     * Returns storage
     *
     * @return storage
     */
    public Storage storage() {
        return storage;
    }

    /**
     * Returns the config supervisor
     *
     * @return config supervisor
     */
    public ConfigSupervisor configSupervisor() {
        return configSupervisor;
    }

    /**
     * Returns user cache
     *
     * @return user cache
     */
    @Override
    public UserCacheImpl userCache() {
        return userCacheImpl;
    }

    /**
     * Returns economy
     *
     * @return economy
     */
    public Economy economy() {
        return economy;
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

    /**
     * Bukkit audiences
     * @return bukkit audiences
     */
    public static BukkitAudiences audiences() {
        return audiences;
    }
}
