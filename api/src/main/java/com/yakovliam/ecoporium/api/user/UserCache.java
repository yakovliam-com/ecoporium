package com.yakovliam.ecoporium.api.user;

import com.github.benmanes.caffeine.cache.AsyncLoadingCache;
import com.yakovliam.ecoporium.api.model.cache.AsyncCache;

import java.util.UUID;

public abstract class UserCache<T extends EcoporiumUser> extends AsyncCache<UUID, T> {

    /**
     * Cache
     *
     * @param cache cache
     */
    public UserCache(AsyncLoadingCache<UUID, T> cache) {
        super(cache);
    }
}
