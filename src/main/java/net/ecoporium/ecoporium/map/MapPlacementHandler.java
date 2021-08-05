package net.ecoporium.ecoporium.map;

import com.github.johnnyjayjay.spigotmaps.RenderedMap;
import com.github.johnnyjayjay.spigotmaps.rendering.ImageRenderer;
import com.github.johnnyjayjay.spigotmaps.util.ImageTools;
import net.ecoporium.ecoporium.EcoporiumPlugin;
import net.ecoporium.ecoporium.api.wrapper.Pair;
import net.ecoporium.ecoporium.model.market.Market;
import net.ecoporium.ecoporium.ticker.info.ScreenInfo;
import net.ecoporium.ecoporium.util.ScreenPositionUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.awt.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MapPlacementHandler implements Listener {

    /**
     * Ecoporium plugin
     */
    private final EcoporiumPlugin plugin;

    /**
     * Queue
     */
    private final Map<UUID, LinkedList<ItemStack>> playerItemPlaceQueue;

    /**
     * Map placement handler
     *
     * @param plugin plugin
     */
    public MapPlacementHandler(EcoporiumPlugin plugin) {
        this.plugin = plugin;
        this.playerItemPlaceQueue = new HashMap<>();

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    /**
     * Create screen
     * <p>
     * Start placing task
     *
     * @param player player
     * @param market market
     * @param symbol symbol
     */
    public void createScreen(Player player, Market market, String symbol) {
        Pair<Integer, Integer> defaultSize = new Pair<>(400, 300);
        // calculate number of maps
        int numberOfMaps = ScreenPositionUtil.calculateNumberOfMaps(new ScreenInfo(defaultSize.getLeft(), defaultSize.getRight()));
        Pair<Integer, Integer> mapScreenSize = ScreenPositionUtil.getWidthHeightMapNumberFromPixelsS(defaultSize);

        plugin.getMessages().ecoporiumStartPlacing.message(player, "%widthmaps%", Integer.toString(mapScreenSize.getLeft()), "%heightmaps%", Integer.toString(mapScreenSize.getRight()));

        // generate default renderers
        List<ImageRenderer> renderers = IntStream.range(0, numberOfMaps)
                .mapToObj((i) -> ImageRenderer.builder()
                        .renderOnce(false)
                        .image(ImageTools.createSingleColoredImage(Color.BLACK))
                        .build()).collect(Collectors.toList());

        List<RenderedMap> renderedMaps = renderers.stream()
                .map(RenderedMap::create)
                .collect(Collectors.toList());

        // generate maps to give to player
        LinkedList<ItemStack> maps = renderedMaps.stream()
                .map(RenderedMap::createItemStack)
                .collect(Collectors.toCollection(LinkedList::new));

        // add all to queue
        playerItemPlaceQueue.put(player.getUniqueId(), maps);

        // give player first item
        LinkedList<ItemStack> items = playerItemPlaceQueue.get(player.getUniqueId());
        ItemStack itemStack = items.getFirst();

        player.getInventory().addItem(itemStack);
        player.updateInventory();
    }

    /**
     * Handles when a player places the next map in the
     *
     * @param event event
     */
    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Entity rightClicked = event.getRightClicked();

        if (!(rightClicked instanceof ItemFrame)) {
            return;
        }

        Player player = event.getPlayer();

        // if player item queue null or empty return
        LinkedList<ItemStack> itemsQueue = playerItemPlaceQueue.get(player.getUniqueId());

        if (itemsQueue == null) {
            return;
        }

        if (itemsQueue.size() <= 0) {
            playerItemPlaceQueue.remove(player.getUniqueId());
            return;
        }

        // if item in hand is first in list
        if (event.getPlayer().getInventory().getItemInMainHand().equals(itemsQueue.getFirst())) {
            // pop
            itemsQueue.pop();

            if (itemsQueue.size() <= 0) {
                return;
            }

            // clear hand
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                player.getInventory().setItemInMainHand(null);
                player.updateInventory();

                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    // get next
                    ItemStack itemStack = itemsQueue.getFirst();
                    player.getInventory().addItem(itemStack);
                    player.updateInventory();
                }, 1L);
            }, 1L);

        }
    }
}
