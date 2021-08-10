package net.ecoporium.ecoporium.storage.implementation.json;

import com.google.common.collect.Table;
import io.leangen.geantyref.TypeToken;
import net.ecoporium.ecoporium.EcoporiumPlugin;
import net.ecoporium.ecoporium.market.Market;
import net.ecoporium.ecoporium.market.stock.StockTicker;
import net.ecoporium.ecoporium.storage.implementation.json.serializer.market.MarketSerializer;
import net.ecoporium.ecoporium.storage.implementation.json.serializer.stock.StockTickerSerializer;
import net.ecoporium.ecoporium.storage.implementation.json.serializer.table.TableSerializer;
import net.ecoporium.ecoporium.storage.implementation.json.serializer.user.EcoporiumUserSerializer;
import net.ecoporium.ecoporium.user.EcoporiumUser;
import org.spongepowered.configurate.BasicConfigurationNode;
import org.spongepowered.configurate.gson.GsonConfigurationLoader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class JsonConfigurationProvider {

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
    public JsonConfigurationProvider(EcoporiumPlugin plugin, String path) {
        this.plugin = plugin;
        // type
        TypeToken<Market<?>> marketType = new io.leangen.geantyref.TypeToken<>() {
        };
        TypeToken<StockTicker<?>> stockTickerType = new io.leangen.geantyref.TypeToken<>() {
        };
        TypeToken<Table<String, String, Integer>> tableType= new TypeToken<>() {
        };

        this.loader = GsonConfigurationLoader.builder()
                .defaultOptions(opts -> opts.serializers(build -> {
                    build.register(EcoporiumUser.class, EcoporiumUserSerializer.getInstance());
                    build.register(stockTickerType, StockTickerSerializer.getInstance());
                    build.register(marketType, MarketSerializer.getInstance());
                    build.register(tableType, TableSerializer.getInstance());
                }))
                .path(resolve(path))
                .build();
    }

    /**
     * Resolves the path of the json storage file
     *
     * @param path path
     * @return path path
     */
    private Path resolve(String path) {
        Path configFile = plugin.getDataFolder().toPath().resolve(path);

        // if the config doesn't exist, create it based on the template in the resources dir
        if (!Files.exists(configFile)) {
            try {
                Files.createDirectories(configFile.getParent());
            } catch (IOException ignored) {
            }

            try (InputStream is = getClass().getClassLoader().getResourceAsStream(path)) {
                Files.copy(Objects.requireNonNull(is), configFile);
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
