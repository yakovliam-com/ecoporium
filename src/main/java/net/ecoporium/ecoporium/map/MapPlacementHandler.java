package net.ecoporium.ecoporium.map;

import com.github.johnnyjayjay.spigotmaps.RenderedMap;
import com.github.johnnyjayjay.spigotmaps.rendering.ImageRenderer;
import net.ecoporium.ecoporium.EcoporiumPlugin;
import net.ecoporium.ecoporium.api.wrapper.Pair;
import net.ecoporium.ecoporium.market.Market;
import net.ecoporium.ecoporium.screen.StaticTickerScreen;
import net.ecoporium.ecoporium.screen.TickerScreen;
import net.ecoporium.ecoporium.screen.data.TickerScreenInfo;
import net.ecoporium.ecoporium.screen.data.TickerScreenMapData;
import net.ecoporium.ecoporium.util.ScreenPositionUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class MapPlacementHandler implements Listener {

    /**
     * Ecoporium plugin
     */
    private final EcoporiumPlugin plugin;

    /**
     * Queue
     * <p>
     * [Player UUID, [Screen UUID, List of ItemStacks]]
     */
    private final Map<UUID, Pair<UUID, LinkedList<ItemStack>>> playerItemPlaceQueue;

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
        // create new screen info object
        TickerScreenInfo tickerScreenInfo = new TickerScreenInfo(640, 640);

        // calculate number of maps per width & height
        Pair<Integer, Integer> mapScreenSize = ScreenPositionUtil.getWidthHeightMapNumberFromPixels(tickerScreenInfo.getWidth(), tickerScreenInfo.getHeight());

        plugin.getMessages().ecoporiumStartPlacing.message(player, "%widthmaps%", Integer.toString(mapScreenSize.getLeft()), "%heightmaps%", Integer.toString(mapScreenSize.getRight()));

        // create ticker
        StaticTickerScreen tickerScreen = new StaticTickerScreen(UUID.randomUUID(), market, symbol, tickerScreenInfo);

        // add to manager
        plugin.getTickerScreenManager().addScreen(tickerScreen.getId(), tickerScreen);

        // get renderers
        List<ImageRenderer> renderers = tickerScreen.getImageRendererList();

        List<RenderedMap> renderedMaps = renderers.stream()
                .map(RenderedMap::create)
                .collect(Collectors.toList());

        // get map ids
        List<Integer> mapIds = renderedMaps.stream()
                .map(r -> r.getView().getId())
                .collect(Collectors.toList());

        // set ticker screen map data
        tickerScreen.setTickerScreenMapData(new TickerScreenMapData(mapIds));

        // initialize maps
        tickerScreen.initializePreCalculatedMaps();

        // generate maps to give to player
        LinkedList<ItemStack> maps = renderedMaps.stream()
                .map(RenderedMap::createItemStack)
                .collect(Collectors.toCollection(LinkedList::new));

        // save to storage
        plugin.getStorage().saveTickerScreen(tickerScreen);

        // add all to queue
        playerItemPlaceQueue.put(player.getUniqueId(), new Pair<>(tickerScreen.getId(), maps));

        // give player first item
        LinkedList<ItemStack> items = playerItemPlaceQueue.get(player.getUniqueId()).getRight();
        ItemStack itemStack = items.getFirst();

        player.getInventory().addItem(itemStack);
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
        Pair<UUID, LinkedList<ItemStack>> fullQueue = playerItemPlaceQueue.get(player.getUniqueId());

        if (fullQueue == null) {
            return;
        }

        LinkedList<ItemStack> itemsQueue = fullQueue.getRight();

        if (itemsQueue.size() <= 0) {
            playerItemPlaceQueue.remove(player.getUniqueId());
            return;
        }

        // if item in hand is first in list
        if (event.getPlayer().getInventory().getItemInMainHand().equals(itemsQueue.getFirst())) {
            // pop
            itemsQueue.pop();

            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                player.getInventory().setItemInMainHand(null);
                player.updateInventory();
            }, 1L);

            if (itemsQueue.size() <= 0) {
                // done placing

                // get screen start rendering
                TickerScreen screen = plugin.getTickerScreenManager().get(fullQueue.getLeft());
                screen.start(plugin);

                return;
            } else {
                // get next item and give to player
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    ItemStack itemStack = itemsQueue.getFirst();
                    player.getInventory().addItem(itemStack);
                    player.updateInventory();
                }, 3L);
            }
        }
    }

    /**
     * Returns the player item place queue
     *
     * @return queue
     */
    public Map<UUID, Pair<UUID, LinkedList<ItemStack>>> getPlayerItemPlaceQueue() {
        return playerItemPlaceQueue;
    }
}
