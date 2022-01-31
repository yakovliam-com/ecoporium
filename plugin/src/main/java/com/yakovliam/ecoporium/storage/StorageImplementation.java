package com.yakovliam.ecoporium.storage;

import com.yakovliam.ecoporium.EcoporiumPlugin;
import com.yakovliam.ecoporium.api.market.Market;
import com.yakovliam.ecoporium.user.EcoporiumUserImpl;

import java.util.UUID;

public interface StorageImplementation {

    void init();

    void shutdown();

    void saveUser(EcoporiumUserImpl user);

    EcoporiumUserImpl loadUser(UUID uuid);

    void saveMarket(Market<?> market);

    void deleteMarket(Market<?> market);

    Market<?> loadMarket(String handle);

    EcoporiumPlugin getPlugin();

}
