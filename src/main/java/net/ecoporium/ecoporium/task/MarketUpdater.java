package net.ecoporium.ecoporium.task;

import net.ecoporium.ecoporium.EcoporiumPlugin;
import net.ecoporium.ecoporium.market.Market;

import java.util.HashMap;
import java.util.Map;

public class MarketUpdater {

    /**
     * Update task map
     */
    private final Map<Market, MarketUpdateTask> updateTaskMap;

    /**
     * Market updater
     *
     * @param plugin plugin
     */
    public MarketUpdater(EcoporiumPlugin plugin) {
        this.updateTaskMap = new HashMap<>();

        // get all markets that are loaded and make a new update task
        plugin.getMarketCache().getMap().forEach((handle, market) -> updateTaskMap.put(market, new MarketUpdateTask(plugin, market)));

        // start all update tasks
        updateTaskMap.forEach((market, task) -> task.start());
    }
}