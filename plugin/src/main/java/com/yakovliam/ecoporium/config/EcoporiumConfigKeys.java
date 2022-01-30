package com.yakovliam.ecoporium.config;

import com.yakovliam.ecoporium.api.config.generic.KeyedConfiguration;
import com.yakovliam.ecoporium.api.config.generic.key.ConfigKey;
import com.yakovliam.ecoporium.api.config.generic.key.SimpleConfigKey;

import java.util.List;

/**
 * All of the {@link ConfigKey}s
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
