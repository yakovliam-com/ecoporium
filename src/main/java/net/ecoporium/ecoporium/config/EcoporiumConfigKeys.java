package net.ecoporium.ecoporium.config;

import net.ecoporium.ecoporium.api.config.generic.KeyedConfiguration;
import net.ecoporium.ecoporium.api.config.generic.key.ConfigKey;
import net.ecoporium.ecoporium.api.config.generic.key.SimpleConfigKey;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static net.ecoporium.ecoporium.api.config.generic.key.ConfigKeyFactory.key;

/**
 * All of the {@link ConfigKey}s used by Space[Plugin Name Here].
 *
 * <p>The {@link #getKeys()} method and associated behaviour allows this class
 * to function a bit like an enum, but with generics.</p>
 */
public final class EcoporiumConfigKeys {

    public static final ConfigKey<Map<String, String>> MARKET_HANDLES_PATHS = key(c -> {
        List<String> paths = c.getKeys("markets", Collections.emptyList());
        return paths.stream()
                .collect(Collectors.toMap(Function.identity(), (p) -> "markets." + p));
    });

    public static final ConfigKey<String> REDIS_URL = key(c -> c.getString("redis.url", null));

    public static final ConfigKey<String> REDIS_TIMESTAMP_KEY = key(c -> c.getString("redis.timestamp-key", null));

    private static final List<SimpleConfigKey<?>> KEYS = KeyedConfiguration.initialise(EcoporiumConfigKeys.class);

    public static List<? extends ConfigKey<?>> getKeys() {
        return KEYS;
    }
}
