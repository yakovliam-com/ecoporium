package net.ecoporium.ecoporium.storage.implementation.json.serializer;

import org.bukkit.map.MapRenderer;

public class MapRendererWrapper {

    /**
     * The id
     */
    private final int id;

    /**
     * Map renderer
     */
    private final MapRenderer mapRenderer;

    /**
     * Map renderer wrapper
     *
     * @param id          id
     * @param mapRenderer renderer
     */
    public MapRendererWrapper(int id, MapRenderer mapRenderer) {
        this.id = id;
        this.mapRenderer = mapRenderer;
    }

    /**
     * Returns the id
     *
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the renderer
     *
     * @return renderer
     */
    public MapRenderer getMapRenderer() {
        return mapRenderer;
    }
}
