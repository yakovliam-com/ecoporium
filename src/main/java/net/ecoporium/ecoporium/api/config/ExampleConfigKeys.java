package net.ecoporium.ecoporium.api.config;

import net.ecoporium.ecoporium.api.config.generic.key.ConfigKey;
import net.ecoporium.ecoporium.api.wrapper.Pair;

import java.util.function.Function;

import static net.ecoporium.ecoporium.api.config.generic.key.ConfigKeyFactory.key;
import static net.ecoporium.ecoporium.api.config.generic.key.ConfigKeyFactory.notReloadable;

/**
 * All of the {@link ConfigKey}s used by Space[Plugin Name Here].
 *
 * <p>The getKeys method and associated behaviour allows this class
 * to function a bit like an enum, but with generics.</p>
 */
public final class ExampleConfigKeys {
    private ExampleConfigKeys() {
    }

    /**
     * The name of the server
     */
    public static final ConfigKey<String> SERVER = key(c -> {
        return c.getString("server", null);
    });

    /**
     * How many minutes to wait between syncs. A value <= 0 will disable syncing.
     */
    public static final ConfigKey<Integer> SYNC_TIME = notReloadable(key(c -> {
        int val = c.getInteger("sync-minutes", -1);
        if (val == -1) {
            val = c.getInteger("data.sync-minutes", -1);
        }
        return val;
    }));

    /**
     * Controls custom objects
     */
    public static final ConfigKey<Pair<String, String>> CUSTOM_OBJECT_KEY = key(c -> {
        String value = c.getString("temporary-add-behaviour", "deny");
        switch (value.toLowerCase()) {
            case "accumulate":
                return new Pair<>("ACCUMULATE", "TEST");
            case "replace":
                return new Pair<>("REPLACE", "TEST");
            default:
                return new Pair<>(null, null);
        }
    });

    /**
     * A functional config key
     */
    public static final ConfigKey<Function<String, String>> PRIMARY_GROUP_CALCULATION = notReloadable(key(c -> {
        String option = "something";
        return null;
    }));

    /**
     * A list of the keys defined in this class.
     * Uncomment this to enable this configuration
     */
//    private static final List<SimpleConfigKey<?>> KEYS = KeyedConfiguration.initialise(ExampleConfigKeys.class);

//    public static List<? extends ConfigKey<?>> getKeys() {
//        return KEYS;
//    }
}
