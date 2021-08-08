package net.ecoporium.ecoporium.task;

import net.ecoporium.ecoporium.EcoporiumPlugin;

public class MarketUpdateTask extends RepeatingTask {

    /**
     * Default period
     * <p>
     * 200 ticks = (10 seconds * 20 ticks/second)
     */
    private static final long DEFAULT_PERIOD = 200L;

    /**
     * Repeating task
     *
     * @param plugin plugin
     */
    public MarketUpdateTask(EcoporiumPlugin plugin) {
        super(plugin, DEFAULT_PERIOD, true);
    }

    @Override
    public void run() {
        plugin.getMarketCache().getCache().synchronous().asMap().values().forEach(market -> {
            market.getTickerCache().forEach((symbol, ticker) -> ticker.update());
        });
    }
}