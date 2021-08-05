package net.ecoporium.ecoporium.storage;

import net.ecoporium.ecoporium.screen.TickerScreen;

import java.util.List;

public class Storage {

    /**
     * Storage implementation
     */
    private final StorageImplementation storageImplementation;

    /**
     * Storage
     *
     * @param storageImplementation storage implementation
     */
    public Storage(StorageImplementation storageImplementation) {
        this.storageImplementation = storageImplementation;
    }

    /**
     * Returns the storage implementation
     *
     * @return implementation
     */
    public StorageImplementation getStorageImplementation() {
        return storageImplementation;
    }

    /**
     * Saves a ticker screen
     *
     * @param tickerScreen ticker screen
     */
    public void saveTickerScreen(TickerScreen tickerScreen) {
        this.storageImplementation.saveTickerScreen(tickerScreen);
    }

    /**
     * Loads ticker screens
     *
     * @return ticker screens
     */
    public List<TickerScreen> loadTickerScreens() {
        return this.storageImplementation.loadTickerScreens();
    }

    /**
     * Deletes a ticker screen
     *
     * @param tickerScreen ticker screen
     */
    public void deleteTickerScreen(TickerScreen tickerScreen) {
        this.storageImplementation.deleteTickerScreen(tickerScreen);
    }

}
