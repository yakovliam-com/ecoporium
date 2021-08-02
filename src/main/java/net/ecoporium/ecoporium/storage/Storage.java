package net.ecoporium.ecoporium.storage;

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
}
