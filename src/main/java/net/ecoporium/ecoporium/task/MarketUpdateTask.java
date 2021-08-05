package net.ecoporium.ecoporium.task;

import net.ecoporium.ecoporium.EcoporiumPlugin;
import net.ecoporium.ecoporium.market.Market;

public class MarketUpdateTask extends RepeatingTask {

    /**
     * Default period
     * <p>
     * 600 ticks = (30 seconds * 20 ticks/second)
     */
    private static final long DEFAULT_PERIOD = 600L;

    /**
     * Applicable market to update
     */
    private final Market market;

    /**
     * Repeating task
     *
     * @param plugin plugin
     */
    public MarketUpdateTask(EcoporiumPlugin plugin, Market market) {
        super(plugin, DEFAULT_PERIOD, true);
        this.market = market;
    }

    @Override
    public void run() {
        // update
        market.getTickerCache().forEach((symbol, ticker) -> ticker.updateStockData(false));
    }
}