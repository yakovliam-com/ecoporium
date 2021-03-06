package com.yakovliam.ecoporium.storage;

import com.yakovliam.ecoporium.api.market.Market;
import com.yakovliam.ecoporium.user.EcoporiumUserImpl;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public record Storage(StorageImplementation storageImplementation) {

    /**
     * Storage
     *
     * @param storageImplementation storage implementation
     */
    public Storage {
    }

    /**
     * Returns the storage implementation
     *
     * @return implementation
     */
    @Override
    public StorageImplementation storageImplementation() {
        return storageImplementation;
    }

    /**
     * Saves a user
     *
     * @param user user
     */
    public void saveUser(EcoporiumUserImpl user, boolean asynchronous) {
        if (asynchronous) {
            CompletableFuture.runAsync(() -> this.storageImplementation.saveUser(user));
        } else {
            this.storageImplementation.saveUser(user);
        }
    }

    /**
     * Loads a user
     *
     * @param uuid user
     */
    public EcoporiumUserImpl loadUser(UUID uuid) {
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
