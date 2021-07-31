package net.ecoporium.ecoporium.storage;

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
     * Saves map plots
     *
     * @param test test
     */
    public void saveMapPlots(List<Void> test) {
        storageImplementation.saveMapPlots(test);
    }
}
