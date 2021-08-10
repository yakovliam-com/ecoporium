package net.ecoporium.ecoporium.storage;


import net.ecoporium.ecoporium.EcoporiumPlugin;
import net.ecoporium.ecoporium.market.Market;
import net.ecoporium.ecoporium.user.EcoporiumUser;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class Storage {

    /**
     * Storage implementation
     */
    private final StorageImplementation storageImplementation;

    /**
     * Storage
     *
     * @param storageImplementation storage implementation
     * @param plugin                plugin
     */
    public Storage(StorageImplementation storageImplementation, EcoporiumPlugin plugin) {
        this.storageImplementation = storageImplementation;
        /**
         * Ecoporium plugin
         */
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
     * Saves a user
     *
     * @param user user
     */
    public void saveUser(EcoporiumUser user) {
        CompletableFuture.runAsync(() -> this.storageImplementation.saveUser(user));
    }

    /**
     * Loads a user
     *
     * @param uuid user
     */
    public EcoporiumUser loadUser(UUID uuid) {
        return this.storageImplementation.loadUser(uuid);
    }

    /**
     * Save market
     *
     * @param market market
     */
    public void saveMarket(Market<?> market) {
        CompletableFuture.runAsync(() -> this.storageImplementation.saveMarket(market));
    }

    /**
     * Deletes a market
     *
     * @param market market
     */
    public void deleteMarket(Market<?> market) {
        CompletableFuture.runAsync(() -> this.storageImplementation.deleteMarket(market));
    }

    /**
     * Loads a market
     *
     * @param handle handle
     * @return market
     */
    public Market<?> loadMarket(String handle) {
        return this.storageImplementation.loadMarket(handle);
    }
}
