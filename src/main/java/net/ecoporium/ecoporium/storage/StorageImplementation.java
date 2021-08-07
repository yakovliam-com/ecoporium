package net.ecoporium.ecoporium.storage;

import net.ecoporium.ecoporium.EcoporiumPlugin;

public interface StorageImplementation {

    void init();

    void shutdown();

    EcoporiumPlugin getPlugin();

}
