package net.ecoporium.ecoporium.config;

import net.ecoporium.ecoporium.api.Plugin;
import net.ecoporium.ecoporium.api.config.generic.KeyedConfiguration;
import net.ecoporium.ecoporium.api.config.generic.adapter.ConfigurationAdapter;

public class EcoporiumConfig extends KeyedConfiguration {

    private final Plugin plugin;

    public EcoporiumConfig(Plugin plugin, ConfigurationAdapter adapter) {
        super(adapter, EcoporiumConfigKeys.getKeys());
        this.plugin = plugin;

        init();
    }

    @Override
    protected void load(boolean initial) {
        super.load(initial);
    }

    @Override
    public void reload() {
        super.reload();
    }

    public Plugin getPlugin() {
        return this.plugin;
    }
}