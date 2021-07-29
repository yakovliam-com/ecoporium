package net.ecoporium.ecoporium.builder;

import net.ecoporium.ecoporium.api.config.generic.adapter.ConfigurationAdapter;

public abstract class ConfigurationContextBuilder<C, V> implements Builder<C, ConfigurationAdapter, V> {

    /**
     * Resolves a configuration path given a previous and current
     *
     * @param previous previous
     * @param n        current
     * @return path
     */
    protected String resolveConfigurationPath(String previous, String n) {
        return previous + "." + n;
    }
}
