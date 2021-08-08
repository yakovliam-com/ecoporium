package net.ecoporium.ecoporium.model.factory;

public interface Factory<K, V> {

    /**
     * Builds a V from K context
     *
     * @param context context
     * @return v
     */
    V build(K context);
}
