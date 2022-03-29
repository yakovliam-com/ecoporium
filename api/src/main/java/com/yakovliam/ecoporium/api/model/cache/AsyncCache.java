package com.yakovliam.ecoporium.api.model.cache;

import com.github.benmanes.caffeine.cache.AsyncLoadingCache;
import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class AsyncCache<K, V> {

    /**
     * Cache
     */
    private final AsyncLoadingCache<K, V> cache;

    /**
     * Cache
     *
     * @param cache cache
     */
    public AsyncCache(AsyncLoadingCache<K, V> cache) {
        this.cache = cache;
    }

    /**
     * Returns the cache
     *
     * @return cache
     */
    public AsyncLoadingCache<K, V> getCache() {
        return cache;
    }
}

