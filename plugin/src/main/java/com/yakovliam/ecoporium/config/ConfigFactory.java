package com.yakovliam.ecoporium.config;

import com.yakovliam.ecoporium.EcoporiumPlugin;
import com.yakovliam.ecoporium.config.config.ConfigHolder;
import com.yakovliam.ecoporium.config.mapper.MiniMessageComponentMapper;
import com.yakovliam.ecoporium.config.messages.MessagesHolder;
import net.kyori.adventure.serializer.configurate4.ConfigurateComponentSerializer;
import net.kyori.adventure.text.Component;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class ConfigFactory {

    /**
     * Ecoporium plugin
     */
    private final EcoporiumPlugin plugin;

    /**
     * Data folder
     */
    private final Path dataFolder;

    /**
     * Config factory
     *
     * @param plugin plugin
     */
    public ConfigFactory(EcoporiumPlugin plugin) {
        this.plugin = plugin;
        this.dataFolder = plugin.getDataFolder().toPath();
    }

    public ConfigHolder config() {
        ConfigHolder config = create(ConfigHolder.class, "config.yml");
        return Objects.requireNonNullElseGet(config, ConfigHolder::new);
    }

    public MessagesHolder messages() {
        MessagesHolder config = create(MessagesHolder.class, "messages.yml");
        return Objects.requireNonNullElseGet(config, MessagesHolder::new);
    }

    private <T> T create(final Class<T> clazz, final String fileName) {
        try {
            if (!Files.exists(dataFolder)) {
                Files.createDirectories(dataFolder);
            }

            Path path = dataFolder.resolve(fileName);

            YamlConfigurationLoader loader = loader(path);
            CommentedConfigurationNode node = loader.load();
            T config = node.get(clazz);

            if (!Files.exists(path)) {
                Files.createFile(path);
                node.set(clazz, config);
            }

            loader.save(node);
            return config;
        } catch (final IOException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    private YamlConfigurationLoader loader(final Path path) {
        return YamlConfigurationLoader.builder()
                .path(path)
                .defaultOptions(options -> options.shouldCopyDefaults(true)
                        .header("https://docs.yakovliam.com/")
                        .serializers(build -> build
                                .register(Component.class, new MiniMessageComponentMapper())
                                .registerAll(ConfigurateComponentSerializer.configurate().serializers())))
                .nodeStyle(NodeStyle.BLOCK)
                .indent(2)
                .build();
    }

}
