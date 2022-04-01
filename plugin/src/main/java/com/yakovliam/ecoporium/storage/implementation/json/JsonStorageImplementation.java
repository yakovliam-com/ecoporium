package com.yakovliam.ecoporium.storage.implementation.json;

import com.google.common.collect.HashBasedTable;
import com.yakovliam.ecoporium.EcoporiumPlugin;
import com.yakovliam.ecoporium.api.market.Market;
import com.yakovliam.ecoporium.storage.StorageImplementation;
import com.yakovliam.ecoporium.user.EcoporiumUserImpl;
import io.leangen.geantyref.TypeToken;
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
    public void saveUser(EcoporiumUserImpl user) {
        ConfigurationNode node = usersProvider.getRoot().node("users");
        // get users list
        try {
            List<EcoporiumUserImpl> userList = node.getList(EcoporiumUserImpl.class);

            // remove if exists
            Objects.requireNonNull(userList).removeIf(u -> u.getUuid().equals(user.getUuid()));
            // add to list
            userList.add(user);
            // save to node
            node.setList(EcoporiumUserImpl.class, userList);

            // save
            save();
        } catch (SerializationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public EcoporiumUserImpl loadUser(UUID uuid) {
        ConfigurationNode node = usersProvider.getRoot().node("users");
        // get users list
        try {
            List<EcoporiumUserImpl> userList = node.getList(EcoporiumUserImpl.class);
            EcoporiumUserImpl ecoporiumUserImpl = Objects.requireNonNull(userList).stream()
                    .filter(u -> u.getUuid().equals(uuid))
                    .findFirst()
                    .orElse(null);

            // if null, create
            if (ecoporiumUserImpl == null) {
                ecoporiumUserImpl = new EcoporiumUserImpl(uuid, HashBasedTable.create());

                // save
                saveUser(ecoporiumUserImpl);
            }

            // return
            return ecoporiumUserImpl;
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

    /**
     * Saves the file
     */
    private synchronized void save() {
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
