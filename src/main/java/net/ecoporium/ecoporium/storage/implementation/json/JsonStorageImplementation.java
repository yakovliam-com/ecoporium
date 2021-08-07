package net.ecoporium.ecoporium.storage.implementation.json;

import net.ecoporium.ecoporium.EcoporiumPlugin;
import net.ecoporium.ecoporium.market.Market;
import net.ecoporium.ecoporium.storage.StorageImplementation;
import net.ecoporium.ecoporium.user.EcoporiumUser;
import org.spongepowered.configurate.ConfigurateException;

import java.util.UUID;

public class JsonStorageImplementation implements StorageImplementation {

    /**
     * Ecoporium plugin
     */
    private final EcoporiumPlugin plugin;

    /**
     * Markets provider
     */
    private final JsonConfigurationProvider marketsProvider;

    /**
     * Users provider
     */
    private final JsonConfigurationProvider usersProvider;

    /**
     * Json storage implementation
     *
     * @param plugin plugin
     */
    public JsonStorageImplementation(EcoporiumPlugin plugin) {
        this.plugin = plugin;
        this.marketsProvider = new JsonConfigurationProvider(plugin, "markets.json");
        this.usersProvider = new JsonConfigurationProvider(plugin, "users.json");

        // init
        init();
    }

    /**
     * Initializes
     */
    @Override
    public void init() {
        // resolves the path which creates the files if they don't already exist
        marketsProvider.load();
        usersProvider.load();
    }

    /**
     * Shuts down
     */
    @Override
    public void shutdown() {
        // save
        save();
    }

    @Override
    public void saveUser(EcoporiumUser user) {
    }

    @Override
    public EcoporiumUser loadUser(UUID uuid) {
        return null;
    }

    @Override
    public void saveMarket(Market market) {
    }

    @Override
    public Market loadMarket(String handle) {
        return null;
    }

    /**
     * Saves the file
     */
    private void save() {
        try {
            marketsProvider.getLoader().save(marketsProvider.getRoot());
            usersProvider.getLoader().save(usersProvider.getRoot());
        } catch (ConfigurateException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the plugin
     *
     * @return plugin
     */
    @Override
    public EcoporiumPlugin getPlugin() {
        return plugin;
    }
}
