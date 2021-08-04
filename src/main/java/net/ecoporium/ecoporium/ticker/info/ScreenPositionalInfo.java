package net.ecoporium.ecoporium.ticker.info;

import org.bukkit.Location;

public class ScreenPositionalInfo {


    /**
     * The point #1 of the item frame screen
     */
    private final Location cornerOne;

    /**
     * The point #2 of the item frame screen
     */
    private final Location cornerTwo;

    /**
     * Screen positional info
     *
     * @param cornerOne corner one
     * @param cornerTwo corner two
     */
    public ScreenPositionalInfo(Location cornerOne, Location cornerTwo) {
        this.cornerOne = cornerOne;
        this.cornerTwo = cornerTwo;
    }

    /**
     * Returns corner one
     *
     * @return corner one
     */
    public Location getCornerOne() {
        return cornerOne;
    }

    /**
     * Corner two
     *
     * @return corner two
     */
    public Location getCornerTwo() {
        return cornerTwo;
    }
}
