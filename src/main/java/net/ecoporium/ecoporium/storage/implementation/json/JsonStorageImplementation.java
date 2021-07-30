package net.ecoporium.ecoporium.storage.implementation.json;

import net.ecoporium.ecoporium.EcoporiumPlugin;
import net.ecoporium.ecoporium.storage.StorageImplementation;
import org.spongepowered.configurate.BasicConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.gson.GsonConfigurationLoader;
import org.spongepowered.configurate.serialize.SerializationException;

import java.io.IOException;
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
        // TODO move to json configuration provider class
        try {
            jsonConfigurationProvider.getLoader().save(jsonConfigurationProvider.getRoot());
        } catch (ConfigurateException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves map plots
     *
     * @param test test
     */
    @Override
    public void saveMapPlots(List<Void> test) {
        try {
            Objects.requireNonNull(jsonConfigurationProvider.getRoot().getList(Void.class)).addAll(test);
        } catch (SerializationException e) {
            e.printStackTrace();
        }

        // TODO move to json configuration provider class
        // save
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
