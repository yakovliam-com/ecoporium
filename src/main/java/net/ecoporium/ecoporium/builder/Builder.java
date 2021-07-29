package net.ecoporium.ecoporium.builder;

public interface Builder<K, C, V> {

    /**
     * Builds a V given K context and C context
     *
     * @param k k
     * @param c c
     * @return v
     */
    V build(K k, C c);
}
