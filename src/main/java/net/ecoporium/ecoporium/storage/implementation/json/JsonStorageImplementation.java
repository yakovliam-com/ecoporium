package net.ecoporium.ecoporium.storage.implementation.json;

import net.ecoporium.ecoporium.EcoporiumPlugin;
import net.ecoporium.ecoporium.storage.StorageImplementation;
import net.ecoporium.ecoporium.screen.TickerScreen;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.List;
import java.util.Objects;

public class JsonStorageImplementation implements StorageImplementation {

    /**
     * Ecoporium plugin
     */
    private final EcoporiumPlugin plugin;

    /**
     * Json file provider
     */
    private final JsonConfigurationProvider jsonConfigurationProvider;

    /**
     * Json storage implementation
     *
     * @param plugin plugin
     */
    public JsonStorageImplementation(EcoporiumPlugin plugin) {
        this.plugin = plugin;
        this.jsonConfigurationProvider = new JsonConfigurationProvider(plugin);

        // init
        init();
    }

    /**
     * Initializes
     */
    @Override
    public void init() {
        // resolves the path which creates the file if it doesn't already exist
        jsonConfigurationProvider.load();
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
    public void saveTickerScreen(TickerScreen tickerScreen) {
        try {
            // add to list
            List<TickerScreen> screenList = jsonConfigurationProvider.getRoot().getList(TickerScreen.class);
            screenList.add(tickerScreen);

            // save
            jsonConfigurationProvider.getRoot().setList(TickerScreen.class, screenList);
            save();
        } catch (SerializationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<TickerScreen> loadTickerScreens() {
        try {
            // return deserialized list
            return jsonConfigurationProvider.getRoot().getList(TickerScreen.class);
        } catch (SerializationException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void deleteTickerScreen(TickerScreen tickerScreen) {
        try {
            // add to list
            List<TickerScreen> screenList = jsonConfigurationProvider.getRoot().getList(TickerScreen.class);

            // get by id and remove
            Objects.requireNonNull(screenList).removeIf(s -> s.getId().equals(tickerScreen.getId()));

            // save
            jsonConfigurationProvider.getRoot().setList(TickerScreen.class, screenList);
            save();
        } catch (SerializationException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves the file
     */
    private void save() {
        try {
            jsonConfigurationProvider.getLoader().save(jsonConfigurationProvider.getRoot());
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
