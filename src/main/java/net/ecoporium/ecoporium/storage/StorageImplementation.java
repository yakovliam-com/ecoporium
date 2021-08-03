package net.ecoporium.ecoporium.storage;

import com.github.johnnyjayjay.spigotmaps.MapStorage;
import net.ecoporium.ecoporium.EcoporiumPlugin;

public interface StorageImplementation {

    void init();

    void shutdown();

    EcoporiumPlugin getPlugin();

}
