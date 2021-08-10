package net.ecoporium.ecoporium.map;

import net.ecoporium.ecoporium.EcoporiumPlugin;
import net.ecoporium.ecoporium.api.wrapper.Pair;
import net.ecoporium.ecoporium.market.Market;
import net.ecoporium.ecoporium.market.stock.StockTicker;
import net.ecoporium.ecoporium.screen.TrendScreen;
import net.ecoporium.ecoporium.screen.info.MapInfo;
import net.ecoporium.ecoporium.screen.info.ScreenInfo;
import net.ecoporium.ecoporium.util.ScreenCalculationUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
     * @param player      player
     * @param market      market
     * @param stockTicker stock ticker
     * @param screenInfo  screen info
     */
    public void createScreen(Player player, Market<?> market, StockTicker<?> stockTicker, ScreenInfo screenInfo) {
        // calculate number of maps per width & height
        Pair<Integer, Integer> mapScreenSize = ScreenCalculationUtil.calculateWidthHeightMaps(screenInfo);
        // calculate number of maps required
        int numberOfMaps = ScreenCalculationUtil.calculateNumberOfMapsRequired(screenInfo);

        plugin.getMessages().placementStartPlacing.message(player, "%widthmaps%", Integer.toString(mapScreenSize.getLeft()), "%heightmaps%", Integer.toString(mapScreenSize.getRight()));

        // create maps
        List<MapView> mapViewList = IntStream.range(0, numberOfMaps)
                .mapToObj(i -> Bukkit.createMap(player.getWorld()))
                .collect(Collectors.toList());

        // get map ids
        List<Integer> mapIds = mapViewList.stream()
                .map(MapView::getId)
                .collect(Collectors.toList());

        // create map info
        MapInfo mapInfo = new MapInfo(mapIds);

        // create screen
        TrendScreen trendScreen = new TrendScreen(UUID.randomUUID(), market, stockTicker, screenInfo, mapInfo);

        // add to manager
        plugin.getTrendScreenManager().addTrendScreen(trendScreen);
        // save to storage
        plugin.getStorage().saveTrendScreen(trendScreen);

        // create map items from mapViews
        LinkedList<ItemStack> maps = new LinkedList<>();
        for (int i = 0; i < mapViewList.size(); i++) {
            maps.add(createItemStack(mapViewList.get(i), i)); // add item stack
        }

        // add all to queue
        playerItemPlaceQueue.put(player.getUniqueId(), new Pair<>(trendScreen.getUuid(), maps));

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
                // done placing, so remove from cache
                this.getPlayerItemPlaceQueue().remove(player.getUniqueId());

                // get screen start rendering
                TrendScreen screen = plugin.getTrendScreenManager().getByUUID(fullQueue.getLeft());
                screen.startScreen(plugin);
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

    /**
     * Creates an item stack map from a view
     *
     * @param mapView  map view
     * @param localeId the id in relation to the other maps in the screen
     * @return item stack map
     */
    private ItemStack createItemStack(MapView mapView, int localeId) {
        ItemStack itemStack = new ItemStack(Material.FILLED_MAP);
        MapMeta mapMeta = (MapMeta) itemStack.getItemMeta();
        Objects.requireNonNull(mapMeta).setMapView(mapView);
        mapMeta.setLore(Collections.singletonList(Integer.toString(localeId)));
        itemStack.setItemMeta(mapMeta);
        return itemStack;
    }
}