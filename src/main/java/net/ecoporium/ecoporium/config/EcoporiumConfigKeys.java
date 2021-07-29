package net.ecoporium.ecoporium.config;

import net.ecoporium.ecoporium.api.config.generic.KeyedConfiguration;
import net.ecoporium.ecoporium.api.config.generic.key.ConfigKey;
import net.ecoporium.ecoporium.api.config.generic.key.SimpleConfigKey;

import java.util.List;

/**
 * All of the {@link ConfigKey}s used by Space[Plugin Name Here].
 *
 * <p>The {@link #getKeys()} method and associated behaviour allows this class
 * to function a bit like an enum, but with generics.</p>
 */
public final class EcoporiumConfigKeys {

    private static final List<SimpleConfigKey<?>> KEYS = KeyedConfiguration.initialise(EcoporiumConfigKeys.class);

    public static List<? extends ConfigKey<?>> getKeys() {
        return KEYS;
    }
}
