package net.ecoporium.ecoporium.storage.implementation.json;

import net.ecoporium.ecoporium.EcoporiumPlugin;
import net.ecoporium.ecoporium.storage.StorageImplementation;
import net.ecoporium.ecoporium.storage.implementation.json.serializer.MapRendererWrapper;
import org.bukkit.map.MapRenderer;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
        // save
        save();
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
