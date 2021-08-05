package net.ecoporium.ecoporium.screen.data;

import java.util.List;

public class TickerScreenMapData {

    /**
     * Map ids of all of the maps associated with a specific ticker screen
     */
    private final List<Integer> mapIds;

    /**
     * Ticker screen map data
     *
     * @param mapIds map ids
     */
    public TickerScreenMapData(List<Integer> mapIds) {
        this.mapIds = mapIds;
    }

    /**
     * Returns map ids
     *
     * @return map ids
     */
    public List<Integer> getMapIds() {
        return mapIds;
    }
}
