package com.yakovliam.ecoporium.api.model.cache;

import com.google.common.cache.LoadingCache;

public abstract class Cache<K, V> {

    /**
     * Cache
     */
    private final LoadingCache<K, V> cache;

    /**
     * Cache
     *
     * @param cache cache
     */
    protected Cache(LoadingCache<K, V> cache) {
        this.cache = cache;
    }

    /**
     * Returns the cache
     *
     * @return cache
     */
    public LoadingCache<K, V> getCache() {
        return cache;
    }
}
