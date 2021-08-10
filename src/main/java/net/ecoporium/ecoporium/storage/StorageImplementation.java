package net.ecoporium.ecoporium.storage;

import net.ecoporium.ecoporium.EcoporiumPlugin;
import net.ecoporium.ecoporium.market.Market;
import net.ecoporium.ecoporium.screen.TrendScreen;
import net.ecoporium.ecoporium.user.EcoporiumUser;

import java.util.List;
import java.util.UUID;

public interface StorageImplementation {

    void init();

    void shutdown();

    void saveUser(EcoporiumUser user);

    EcoporiumUser loadUser(UUID uuid);

    void saveMarket(Market<?> market);

    void deleteMarket(Market<?> market);

    Market<?> loadMarket(String handle);

    List<TrendScreen> loadTrendScreens();

    void saveTrendScreen(TrendScreen trendScreen);

    void deleteTrendScreen(TrendScreen trendScreen);

    EcoporiumPlugin getPlugin();

}
