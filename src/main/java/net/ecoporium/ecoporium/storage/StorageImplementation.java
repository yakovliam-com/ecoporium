package net.ecoporium.ecoporium.storage;

import net.ecoporium.ecoporium.EcoporiumPlugin;
import net.ecoporium.ecoporium.screen.TickerScreen;

import java.util.List;

public interface StorageImplementation {

    void init();

    void shutdown();

    void saveTickerScreen(TickerScreen tickerScreen);

    List<TickerScreen> loadTickerScreens();

    void deleteTickerScreen(TickerScreen tickerScreen);

    EcoporiumPlugin getPlugin();

}
