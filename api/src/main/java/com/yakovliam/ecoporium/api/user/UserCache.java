package com.yakovliam.ecoporium.api.user;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.yakovliam.ecoporium.api.functional.ObjectiveConsumer;
import com.yakovliam.ecoporium.api.model.cache.AsyncCache;

import java.util.UUID;

public abstract class UserCache<T extends EcoporiumUser> extends AsyncCache<UUID, T> {

    /**
     * Cache
     *
     * @param consumer objective consumer
     */
    public UserCache(ObjectiveConsumer<UUID, T> consumer) {
        super(Caffeine.newBuilder().buildAsync((consumer::accept)));
    }
}
