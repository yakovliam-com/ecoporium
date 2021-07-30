package net.ecoporium.ecoporium.storage;

import net.ecoporium.ecoporium.EcoporiumPlugin;

import java.util.List;

public interface StorageImplementation {

    void init();

    void shutdown();

    void saveMapPlots(List<Void> test);

    EcoporiumPlugin getPlugin();

}
