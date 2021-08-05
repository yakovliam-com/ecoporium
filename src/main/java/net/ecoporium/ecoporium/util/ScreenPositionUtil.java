package net.ecoporium.ecoporium.util;

import net.ecoporium.ecoporium.api.wrapper.Pair;
import net.ecoporium.ecoporium.ticker.info.ScreenInfo;
import org.bukkit.Location;
import org.bukkit.Rotation;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

public class ScreenPositionUtil {

    /**
     * Calculate screen number of maps
     *
     * @param info info
     * @return number of maps
     */
    public static int calculateNumberOfMaps(ScreenInfo info) {
        int widthPixels = info.getWidth();
        int heightPixels = info.getHeight();

        int widthMaps = (int) Math.round((double) widthPixels / 128);
        int heightMaps = (int) Math.round((double) heightPixels / 128);

        return widthMaps * heightMaps;
    }

    /**
     * Get width / height in maps from pixels
     *
     * @param widthHeightPixels width height pixels
     * @return map number
     */
    public static Pair<Integer, Integer> getWidthHeightMapNumberFromPixels(Pair<Integer, Integer> widthHeightPixels) {
        int widthPixels = widthHeightPixels.getLeft();
        int heightPixels = widthHeightPixels.getRight();

        int widthMaps = (int) Math.round((double) widthPixels / 128);
        int heightMaps = (int) Math.round((double) heightPixels / 128);

        return new Pair<>(widthMaps, heightMaps);
    }

    /**
     * If corners have the same XY or ZY
     *
     * @param cornerOne corner one
     * @param cornerTwo corner two
     * @return if corners have the same XY or ZY
     */
    public static boolean cornersHaveSameXYOrZY(Location cornerOne, Location cornerTwo) {
        return ((cornerOne.getBlockX() == cornerTwo.getBlockX()) && (cornerOne.getBlockY() == cornerTwo.getBlockY())) ||
                (((cornerOne.getBlockZ() == cornerTwo.getBlockZ()) && (cornerOne.getBlockY() == cornerTwo.getBlockY())));
    }


    /**
     * Gets a block difference
     *
     * @param one one
     * @param two two
     * @return block diff
     */
    public static int getBlockDiff(int one, int two) {
        return Math.abs(two - one);
    }

    /**
     * Gets a block difference
     *
     * @param one one
     * @param two two
     * @return block diff, real (not abs)
     */
    public static int getRealBlockDiff(int one, int two) {
        return two - one;
    }

    /**
     * If two corners have a bad shape
     *
     * @param cornerOne corner one
     * @param cornerTwo corner two
     * @return bad shape
     */
    public static boolean isBadShape(Location cornerOne, Location cornerTwo) {
        return ((getBlockDiff(cornerOne.getBlockX(), cornerTwo.getBlockX()) >= 1  // bad shape
                && getBlockDiff(cornerOne.getBlockZ(), cornerTwo.getBlockZ()) >= 1)
                || cornersHaveSameXYOrZY(cornerOne, cornerTwo)); // same x & y OR z & y plane

    }

    /**
     * Is axis aligned
     *
     * @param face face
     * @return aligned
     */
    public static boolean isAxisAligned(BlockFace face) {
        switch (face) {
            case DOWN:
            case UP:
            case WEST:
            case EAST:
            case SOUTH:
            case NORTH:
                return true;
            default:
                return false;
        }
    }

    /**
     * Is between
     *
     * @param value  value
     * @param bound1 bound 1
     * @param bound2 bound 2
     * @return is between
     */
    public static boolean isBetween(int value, int bound1, int bound2) {
        return (bound1 > bound2 && value >= bound2 && value <= bound1) || (bound1 < bound2 && value <= bound2 && value >= bound1);
    }

    /**
     * Is between
     *
     * @param value  value
     * @param bound1 bound 1
     * @param bound2 bound 2
     * @return is between
     */
    public static boolean isBetween(double value, double bound1, double bound2) {
        return (bound1 > bound2 && value >= bound2 && value <= bound1) || (bound1 < bound2 && value <= bound2 && value >= bound1);
    }

    /**
     * Calculate width direction
     *
     * @param player player
     * @param face   face
     * @return block face
     */
    public static BlockFace calculateWidthDirection(Player player, BlockFace face) {
        float yaw = (360.0f + player.getLocation().getYaw()) % 360.0f;
        switch (face) {
            case NORTH:
                return BlockFace.WEST;
            case SOUTH:
                return BlockFace.EAST;
            case EAST:
                return BlockFace.NORTH;
            case WEST:
                return BlockFace.SOUTH;
            case UP:
            case DOWN:
                if (isBetween(yaw, 45.0, 135.0))
                    return BlockFace.NORTH;
                else if (isBetween(yaw, 135.0, 225.0))
                    return BlockFace.EAST;
                else if (isBetween(yaw, 225.0, 315.0))
                    return BlockFace.SOUTH;
                else
                    return BlockFace.WEST;
            default:
                return null;
        }
    }

    /**
     * Calculate height direction
     *
     * @param player player
     * @param face   face
     * @return block face
     */
    public static BlockFace calculateHeightDirection(Player player, BlockFace face) {
        float yaw = (360.0f + player.getLocation().getYaw()) % 360.0f;
        switch (face) {
            case NORTH:
            case SOUTH:
            case EAST:
            case WEST:
                return BlockFace.DOWN;
            case UP:
                if (isBetween(yaw, 45.0, 135.0))
                    return BlockFace.EAST;
                else if (isBetween(yaw, 135.0, 225.0))
                    return BlockFace.SOUTH;
                else if (isBetween(yaw, 225.0, 315.0))
                    return BlockFace.WEST;
                else
                    return BlockFace.NORTH;
            case DOWN:
                if (isBetween(yaw, 45.0, 135.0))
                    return BlockFace.WEST;
                else if (isBetween(yaw, 135.0, 225.0))
                    return BlockFace.NORTH;
                else if (isBetween(yaw, 225.0, 315.0))
                    return BlockFace.EAST;
                else
                    return BlockFace.SOUTH;
            default:
                return null;
        }
    }

    /**
     * Find rotation facing
     *
     * @param heightDirection height direction
     * @param widthDirection  width direction
     * @return rotation
     */
    public static Rotation facingToRotation(BlockFace heightDirection, BlockFace widthDirection) {
        switch (heightDirection) {
            case WEST:
                return Rotation.CLOCKWISE_45;
            case NORTH:
                return widthDirection == BlockFace.WEST ? Rotation.CLOCKWISE : Rotation.NONE;
            case EAST:
                return Rotation.CLOCKWISE_135;
            case SOUTH:
                return widthDirection == BlockFace.WEST ? Rotation.CLOCKWISE : Rotation.NONE;
            default:
                return Rotation.NONE;
        }
    }
}
