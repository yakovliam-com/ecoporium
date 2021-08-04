package net.ecoporium.ecoporium.util;

import net.ecoporium.ecoporium.ticker.info.ScreenPositionalInfo;
import org.bukkit.Location;

public class ScreenPositionUtil {

    /**
     * Calculate screen number of maps
     * <p>
     * Returns null if the shape is 3d, not 2d
     *
     * @param positionalInfo positional info
     * @return number of maps
     */
    public static Integer calculateNumberOfMaps(ScreenPositionalInfo positionalInfo) {
        Location cornerOne = positionalInfo.getCornerOne();
        Location cornerTwo = positionalInfo.getCornerTwo();

        // get x diff
        int xDiff = Math.abs(cornerOne.getBlockX() - cornerTwo.getBlockX());
        // get z diff
        int zDiff = Math.abs(cornerOne.getBlockZ() - cornerTwo.getBlockZ());

        boolean isAlongX = false;
        boolean isAlongZ = false;

        // if abs(x difference) is positive, then they're along the Z
        if (xDiff >= 1) {
            isAlongZ = true;
        }
        // if abs(z difference) is positive, then they're along the X
        if (zDiff >= 1) {
            isAlongX = true;
        }

        // if both true, then something went wrong (they made a 3d shape, not 2d)
        if (isAlongX && isAlongZ) {
            return null;
        }

        // find bottom side length
        int bottomSideLength;

        if (isAlongX) {
            bottomSideLength = xDiff;
        } else if (isAlongZ) {
            bottomSideLength = zDiff;
        } else {
            // if the same place, then the side length is 1
            bottomSideLength = 1;
        }

        // find side side length
        int sideSideLength = Math.abs(cornerOne.getBlockY() - cornerTwo.getBlockY());

        // if there's o 'block difference' then the side length is 1
        if (sideSideLength == 0) {
            sideSideLength = 1;
        }

        // calcualte actual number of maps
        return bottomSideLength * sideSideLength;
    }
}
