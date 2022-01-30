package com.yakovliam.ecoporium.storage;

import com.yakovliam.ecoporium.EcoporiumPlugin;
import com.yakovliam.ecoporium.market.Market;
import com.yakovliam.ecoporium.user.EcoporiumUser;

import java.util.UUID;

public interface StorageImplementation {

    void init();

    void shutdown();

    void saveUser(EcoporiumUser user);

    EcoporiumUser loadUser(UUID uuid);

    void saveMarket(Market<?> market);

    void deleteMarket(Market<?> market);

    Market<?> loadMarket(String handle);

    EcoporiumPlugin getPlugin();

}
