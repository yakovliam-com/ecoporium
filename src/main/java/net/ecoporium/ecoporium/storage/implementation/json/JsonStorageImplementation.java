package net.ecoporium.ecoporium.storage.implementation.json;

import com.google.common.collect.HashBasedTable;
import io.leangen.geantyref.TypeToken;
import net.ecoporium.ecoporium.EcoporiumPlugin;
import net.ecoporium.ecoporium.market.Market;
import net.ecoporium.ecoporium.screen.TrendScreen;
import net.ecoporium.ecoporium.storage.StorageImplementation;
import net.ecoporium.ecoporium.user.EcoporiumUser;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.List;
import java.util.Objects;
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
     * Screens provider
     */
    private final JsonConfigurationProvider trendScreensProvider;

    /**
     * Market type
     */
    private final TypeToken<Market<?>> marketType = new io.leangen.geantyref.TypeToken<>() {
    };

    /**
     * Json storage implementation
     *
     * @param plugin plugin
     */
    public JsonStorageImplementation(EcoporiumPlugin plugin) {
        this.plugin = plugin;
        this.marketsProvider = new JsonConfigurationProvider(plugin, "markets.json");
        this.usersProvider = new JsonConfigurationProvider(plugin, "users.json");
        this.trendScreensProvider = new JsonConfigurationProvider(plugin, "trend-screens.json");

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
        trendScreensProvider.load();
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
        ConfigurationNode node = usersProvider.getRoot().node("users");
        // get users list
        try {
            List<EcoporiumUser> userList = node.getList(EcoporiumUser.class);

            // remove if exists
            Objects.requireNonNull(userList).removeIf(u -> u.getUuid().equals(user.getUuid()));
            // add to list
            userList.add(user);
            // save to node
            node.setList(EcoporiumUser.class, userList);

            // save
            save();
        } catch (SerializationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public EcoporiumUser loadUser(UUID uuid) {
        ConfigurationNode node = usersProvider.getRoot().node("users");
        // get users list
        try {
            List<EcoporiumUser> userList = node.getList(EcoporiumUser.class);
            EcoporiumUser ecoporiumUser = Objects.requireNonNull(userList).stream()
                    .filter(u -> u.getUuid().equals(uuid))
                    .findFirst()
                    .orElse(null);

            // if null, create
            if (ecoporiumUser == null) {
                ecoporiumUser = new EcoporiumUser(uuid, HashBasedTable.create());

                // save
                saveUser(ecoporiumUser);
            }

            // return
            return ecoporiumUser;
        } catch (SerializationException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void saveMarket(Market<?> market) {
        ConfigurationNode node = marketsProvider.getRoot().node("markets");
        // get markets list
        try {
            List<Market<?>> marketList = node.getList(marketType);

            // remove if exists
            Objects.requireNonNull(marketList).removeIf(m -> m.getHandle().equals(market.getHandle()));
            // add to list
            marketList.add(market);
            // save to node
            node.setList(marketType, marketList);

            // save
            save();
        } catch (SerializationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteMarket(Market<?> market) {
        ConfigurationNode node = marketsProvider.getRoot().node("markets");
        // get markets list
        try {
            List<Market<?>> marketList = node.getList(marketType);

            // remove if exists
            Objects.requireNonNull(marketList).removeIf(m -> m.getHandle().equals(market.getHandle()));
            // save to node
            node.setList(marketType, marketList);

            // save
            save();
        } catch (SerializationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Market<?> loadMarket(String handle) {
        ConfigurationNode node = marketsProvider.getRoot().node("markets");
        // get markets list
        try {
            List<Market<?>> marketList = node.getList(marketType);
            return Objects.requireNonNull(marketList).stream()
                    .filter(m -> m.getHandle().equals(handle))
                    .findFirst()
                    .orElse(null);
        } catch (SerializationException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<TrendScreen> loadTrendScreens() {
        ConfigurationNode node = trendScreensProvider.getRoot().node("screens");
        // get screens list
        try {
            return node.getList(TrendScreen.class);
        } catch (SerializationException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void saveTrendScreen(TrendScreen trendScreen) {
        ConfigurationNode node = trendScreensProvider.getRoot().node("screens");
        // get screens list
        try {
            List<TrendScreen> trendScreens = node.getList(TrendScreen.class);
            // remove trend screen that we're saving back (if it exists)
            Objects.requireNonNull(trendScreens).removeIf(t -> t.getUuid().equals(trendScreen.getUuid()));
            // add back to list
            trendScreens.add(trendScreen);
            // set list
            node.setList(TrendScreen.class, trendScreens);
            // save
            save();
        } catch (SerializationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteTrendScreen(TrendScreen trendScreen) {
        ConfigurationNode node = trendScreensProvider.getRoot().node("screens");
        // get screens list
        try {
            List<TrendScreen> trendScreens = node.getList(TrendScreen.class);
            // remove trend screen
            Objects.requireNonNull(trendScreens).removeIf(t -> t.getUuid().equals(trendScreen.getUuid()));
            // set list
            node.setList(TrendScreen.class, trendScreens);
            // save
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
            marketsProvider.getLoader().save(marketsProvider.getRoot());
        } catch (ConfigurateException e) {
            e.printStackTrace();
        }

        try {
            usersProvider.getLoader().save(usersProvider.getRoot());
        } catch (ConfigurateException e) {
            e.printStackTrace();
        }

        try {
            trendScreensProvider.getLoader().save(trendScreensProvider.getRoot());
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
