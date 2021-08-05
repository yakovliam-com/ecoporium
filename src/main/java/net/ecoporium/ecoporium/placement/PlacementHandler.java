package net.ecoporium.ecoporium.placement;

import com.github.johnnyjayjay.spigotmaps.RenderedMap;
import com.github.johnnyjayjay.spigotmaps.rendering.ImageRenderer;
import net.ecoporium.ecoporium.EcoporiumPlugin;
import net.ecoporium.ecoporium.api.wrapper.Pair;
import net.ecoporium.ecoporium.ticker.StaticTickerScreen;
import net.ecoporium.ecoporium.ticker.info.ScreenInfo;
import net.ecoporium.ecoporium.util.ScreenPositionUtil;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Hanging;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class PlacementHandler {

    /**
     * Placement data map
     */
    private final Map<UUID, PlacementData> placementDataMap;

    /**
     * Ecoporium plugin
     */
    private final EcoporiumPlugin plugin;

    /**
     * Placement handler
     *
     * @param plugin plugin
     */
    public PlacementHandler(EcoporiumPlugin plugin) {
        this.plugin = plugin;
        this.placementDataMap = new HashMap<>();
    }

    /**
     * Is waiting to place
     *
     * @param uuid uuid
     * @return is waiting to place
     */
    public boolean isWaitingToPlace(UUID uuid) {
        return this.placementDataMap.containsKey(uuid);
    }

    /**
     * Gets placement data
     *
     * @param uuid uuid
     * @return placement data
     */
    public PlacementData get(UUID uuid) {
        return this.placementDataMap.getOrDefault(uuid, null);
    }

    /**
     * Removes placement data (waiting)
     *
     * @param uuid uuid
     */
    public void remove(UUID uuid) {
        this.placementDataMap.remove(uuid);
    }

    /**
     * Adds to waiting
     *
     * @param uuid uuid
     * @param data data
     */
    public void add(UUID uuid, PlacementData data) {
        this.placementDataMap.put(uuid, data);
    }

    /**
     * Places a screen
     *
     * @param player       player
     * @param clickedBlock clicked block
     * @param blockFace    block face
     */
    public PlacementResult placeScreen(Player player, Block clickedBlock, BlockFace blockFace) {
        if (!ScreenPositionUtil.isAxisAligned(blockFace)) {
            return PlacementResult.INVALID_FACING;
        }

        if (blockFace.getModY() != 0) {
            return PlacementResult.INVALID_FACING;
        }

        Block b = clickedBlock.getRelative(blockFace);

        PlacementData data = get(player.getUniqueId());

        // 400x300
        Pair<Integer, Integer> screenSizePixels = new Pair<>(data.getScreenInfo().getWidth(), data.getScreenInfo().getHeight());
        Pair<Integer, Integer> size = ScreenPositionUtil.getWidthHeightMapNumberFromPixelsS(screenSizePixels);

        BlockFace widthDirection = ScreenPositionUtil.calculateWidthDirection(player, blockFace);
        BlockFace heightDirection = ScreenPositionUtil.calculateHeightDirection(player, blockFace);

        if (widthDirection == null || heightDirection == null)
            return PlacementResult.INVALID_DIRECTION;

        // check for space
        for (int x = 0; x < size.getLeft(); x++) {
            for (int y = 0; y < size.getRight(); y++) {
                Block frameBlock = b.getRelative(widthDirection, x).getRelative(heightDirection, y);

                if (!clickedBlock.getRelative(widthDirection, x).getRelative(heightDirection, y).getType().isSolid())
                    return PlacementResult.INSUFFICIENT_WALL;
                if (frameBlock.getType().isSolid())
                    return PlacementResult.INSUFFICIENT_SPACE;
                if (!b.getWorld().getNearbyEntities(frameBlock.getLocation().add(0.5, 0.5, 0.5), 0.5, 0.5, 0.5, Hanging.class::isInstance).isEmpty())
                    return PlacementResult.OVERLAPPING_ENTITY;
            }
        }

        // generate actual ticker screen
        StaticTickerScreen tickerScreen = new StaticTickerScreen(plugin, UUID.randomUUID(), data.getSymbol(), new ScreenInfo(screenSizePixels.getLeft(), screenSizePixels.getRight()));
        Iterator<ImageRenderer> rendererIterator = tickerScreen.getImageRendererList().listIterator();

        // spawn item frame
        for (int x = 0; x < size.getLeft(); x++) {
            for (int y = 0; y < size.getRight(); y++) {
                ItemFrame frame = clickedBlock.getWorld().spawn(b.getRelative(widthDirection, x).getRelative(heightDirection, y).getLocation(), ItemFrame.class);
                frame.setFacingDirection(blockFace);

                // create item
                RenderedMap map = RenderedMap.create(rendererIterator.next());

                frame.setItem(map.createItemStack());
                frame.setRotation(ScreenPositionUtil.facingToRotation(heightDirection, widthDirection));

                // frame.setFixed(true);
                // frame.setVisible(false);
            }
        }

        // TODO save ticker screens to storage, WITH BUKKIT MAP ID
        // TODO save to manager somehow

        remove(player.getUniqueId());

        return PlacementResult.SUCCESS;
    }

}
