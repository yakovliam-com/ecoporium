package net.ecoporium.ecoporium.storage;


import net.ecoporium.ecoporium.market.Market;
import net.ecoporium.ecoporium.user.EcoporiumUser;

import java.util.UUID;

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
     * Saves a user
     *
     * @param user user
     */
    public void saveUser(EcoporiumUser user) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Loads a user
     *
     * @param uuid user
     */
    public EcoporiumUser loadUser(UUID uuid) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Save market
     *
     * @param market market
     */
    public void saveMarket(Market market) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Loads a market
     *
     * @param handle handle
     * @return market
     */
    public Market loadMarket(String handle) {
        throw new RuntimeException("Not implemented");
    }
}
