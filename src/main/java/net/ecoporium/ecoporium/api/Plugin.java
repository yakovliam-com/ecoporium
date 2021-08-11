package net.ecoporium.ecoporium.api;

import net.ecoporium.ecoporium.api.config.adapter.BukkitConfigAdapter;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public abstract class Plugin extends JavaPlugin {

    /**
     * Resolves a configuration
     *
     * @param fileName file name
     * @return configuration path
     */
    private Path resolveConfig(String fileName) {
        Path configFile = getDataFolder().toPath().resolve(fileName);

        // if the config doesn't exist, create it based on the template in the resources dir
        if (!Files.exists(configFile)) {
            try {
                Files.createDirectories(configFile.getParent());
            } catch (IOException ignored) {
            }

            try (InputStream is = getClass().getClassLoader().getResourceAsStream(fileName)) {
                Files.copy(Objects.requireNonNull(is), configFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return configFile;
    }

    /**
     * Provides a configuration adapter
     *
     * @param fileName file name
     * @return config adapter
     */
    protected BukkitConfigAdapter provideConfigAdapter(String fileName) {
        return new BukkitConfigAdapter(this, resolveConfig(fileName).toFile());
    }
}
