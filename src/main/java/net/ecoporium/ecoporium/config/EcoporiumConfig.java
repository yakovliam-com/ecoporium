package net.ecoporium.ecoporium.config;

import net.ecoporium.ecoporium.api.Plugin;
import net.ecoporium.ecoporium.api.config.generic.KeyedConfiguration;
import net.ecoporium.ecoporium.api.config.generic.adapter.ConfigurationAdapter;

public class EcoporiumConfig extends KeyedConfiguration {

    private final Plugin plugin;

    private final ConfigurationAdapter adapter;

    public EcoporiumConfig(Plugin plugin, ConfigurationAdapter adapter) {
        super(adapter, EcoporiumConfigKeys.getKeys());
        this.plugin = plugin;
        this.adapter = adapter;

        init();
    }

    @Override
    protected void load(boolean initial) {
        super.load(initial);
    }

    public Plugin getPlugin() {
        return this.plugin;
    }

    public ConfigurationAdapter getAdapter() {
        return adapter;
    }
}