package net.ecoporium.ecoporium.screen.info;

import java.util.List;

public class MapInfo {

    /**
     * Map ids associated with the screen
     */
    private final List<Integer> mapIds;

    /**
     * Map info
     *
     * @param mapIds map ids
     */
    public MapInfo(List<Integer> mapIds) {
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
