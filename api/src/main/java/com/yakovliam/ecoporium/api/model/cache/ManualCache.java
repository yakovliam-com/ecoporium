package com.yakovliam.ecoporium.api.model.cache;

import java.util.Map;
import java.util.function.Predicate;

public class ManualCache<K, V> {

    /**
     * Map cache
     */
    private Map<K, V> map;

    /**
     * Manual cache
     *
     * @param map map
     */
    public ManualCache(Map<K, V> map) {
        this.map = map;
    }

    /**
     * Returns the map
     *
     * @return map
     */
    public Map<K, V> map() {
        return map;
    }

    /**
     * Sets the map
     *
     * @param map map
     */
    public void map(Map<K, V> map) {
        this.map = map;
    }

    /**
     * Returns a value by key
     *
     * @param key key
     * @param def default
     * @return value
     */
    public V get(K key, V def) {
        return map.getOrDefault(key, def);
    }

    /**
     * Find a value by a predicate
     *
     * @param predicate predicate
     * @param def       default
     * @return value
     */
    public V findByPredicate(Predicate<? super Map.Entry<K, V>> predicate, V def) {
        return map.entrySet().stream()
                .filter(predicate)
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(def);
    }
}
