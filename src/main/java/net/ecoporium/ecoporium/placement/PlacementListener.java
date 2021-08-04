package net.ecoporium.ecoporium.placement;

import net.ecoporium.ecoporium.EcoporiumPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlacementListener implements Listener {

    /**
     * Ecoporium plugin
     */
    private final EcoporiumPlugin plugin;

    /**
     * Placement listener
     *
     * @param plugin plugin
     */
    public PlacementListener(EcoporiumPlugin plugin) {
        this.plugin = plugin;

        // register
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPLayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (!plugin.getPlacementHandler().isWaitingToPlace(player.getUniqueId())) {
            return;
        }

        if (event.getAction() == Action.RIGHT_CLICK_AIR) {
            plugin.getMessages().ecoporiumPlacementCanceled.message(player);
            return;
        }

        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        event.setCancelled(true);

        // place screen
        PlacementResult result = plugin.getPlacementHandler().placeScreen(player, event.getClickedBlock(), event.getBlockFace());

        switch (result) {
            case INVALID_FACING:
                plugin.getMessages().ecoporiumPlacementInvalidFacing.message(player);
                break;
            case INVALID_DIRECTION:
                plugin.getMessages().ecoporiumPlacementInvalidDirection.message(player);
                break;
            case INSUFFICIENT_SPACE:
            case INSUFFICIENT_WALL:
                plugin.getMessages().ecoporiumPlacementInsufficientWall.message(player);
                break;
            case OVERLAPPING_ENTITY:
                plugin.getMessages().ecoporiumPlacementOverlappingEntity.message(player);
                break;
            case SUCCESS:
                plugin.getMessages().ecoporiumPlacementSuccess.message(player);
                break;
        }
    }
}
