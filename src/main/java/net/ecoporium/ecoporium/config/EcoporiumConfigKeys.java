package net.ecoporium.ecoporium.config;

import net.ecoporium.ecoporium.api.config.generic.KeyedConfiguration;
import net.ecoporium.ecoporium.api.config.generic.key.ConfigKey;
import net.ecoporium.ecoporium.api.config.generic.key.SimpleConfigKey;
import net.ecoporium.ecoporium.api.wrapper.Pair;
import net.ecoporium.ecoporium.util.ColorUtil;

import java.awt.*;
import java.util.List;

import static net.ecoporium.ecoporium.api.config.generic.key.ConfigKeyFactory.key;

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
