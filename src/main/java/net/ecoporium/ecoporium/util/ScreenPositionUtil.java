package net.ecoporium.ecoporium.util;

import net.ecoporium.ecoporium.api.wrapper.Pair;
import net.ecoporium.ecoporium.screen.data.TickerScreenInfo;
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
    public static int calculateNumberOfMaps(TickerScreenInfo info) {
        int widthPixels = info.getWidth();
        int heightPixels = info.getHeight();

        int widthMaps = (int) Math.round((double) widthPixels / 128);
        int heightMaps = (int) Math.round((double) heightPixels / 128);

        return widthMaps * heightMaps;
    }

    /**
     * Get width / height in maps from pixels
     *
     * @param widthPixels  width pixels
     * @param heightPixels height pixels
     * @return map number
     */
    public static Pair<Integer, Integer> getWidthHeightMapNumberFromPixels(int widthPixels, int heightPixels) {
        int widthMaps = (int) Math.round((double) widthPixels / 128);
        int heightMaps = (int) Math.round((double) heightPixels / 128);

        return new Pair<>(widthMaps, heightMaps);
    }
}
