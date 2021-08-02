package net.ecoporium.ecoporium.storage.implementation.json;

import net.ecoporium.ecoporium.EcoporiumPlugin;
import net.ecoporium.ecoporium.storage.implementation.json.serializer.MapRendererSerializer;
import net.ecoporium.ecoporium.storage.implementation.json.serializer.MapRendererWrapper;
import org.spongepowered.configurate.BasicConfigurationNode;
import org.spongepowered.configurate.gson.GsonConfigurationLoader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class JsonConfigurationProvider {

    private static final String PATH = "plots.json";

    /**
     * Ecoporium plugin
     */
    private final EcoporiumPlugin plugin;

    /**
     * Loader
     */
    private final GsonConfigurationLoader loader;

    /**
     * Root node
     */
    private BasicConfigurationNode root;

    /**
     * Json file provider
     *
     * @param plugin plugin
     */
    public JsonConfigurationProvider(EcoporiumPlugin plugin) {
        this.plugin = plugin;
        this.loader = GsonConfigurationLoader.builder()
                .defaultOptions(opts -> opts.serializers(build -> build.register(MapRendererWrapper.class, new MapRendererSerializer())))
                .path(resolve())
                .build();
    }

    /**
     * Resolves the path of the json storage file
     *
     * @return path
     */
    private Path resolve() {
        Path configFile = plugin.getDataFolder().toPath().resolve(PATH);

        // if the config doesn't exist, create it based on the template in the resources dir
        if (!Files.exists(configFile)) {
            try {
                Files.createDirectories(configFile.getParent());
            } catch (IOException ignored) {
            }

            try (InputStream is = getClass().getClassLoader().getResourceAsStream(PATH)) {
                Files.copy(is, configFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return configFile;
    }

    /**
     * Loads the json configuration
     */
    public void load() {
        try {
            this.root = loader.load();
        } catch (IOException e) {
            System.err.println("An error occurred while loading the json storage implementation: " + e.getMessage());
            if (e.getCause() != null) {
                e.getCause().printStackTrace();
            }
        }
    }

    public GsonConfigurationLoader getLoader() {
        return loader;
    }

    public BasicConfigurationNode getRoot() {
        return root;
    }
}
