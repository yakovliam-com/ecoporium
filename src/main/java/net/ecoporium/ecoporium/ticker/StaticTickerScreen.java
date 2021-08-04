package net.ecoporium.ecoporium.ticker;

import net.ecoporium.ecoporium.EcoporiumPlugin;
import net.ecoporium.ecoporium.model.market.StockTicker;
import net.ecoporium.ecoporium.ticker.fetch.StockTickerFetcher;
import net.ecoporium.ecoporium.ticker.info.ScreenPositionalInfo;
import net.ecoporium.ecoporium.ticker.task.TickerScreenUpdateTask;

import java.util.UUID;

public class StaticTickerScreen extends TickerScreen {

    /**
     * Symbol
     */
    private final String symbol;

    /**
     * Static ticker screen
     * <p>
     * Once initialized, this screen will display the applicable symbol's data, and that data only
     *
     * @param plugin         plugin
     * @param id             id
     * @param symbol         symbol
     * @param positionalInfo positional info
     */
    public StaticTickerScreen(EcoporiumPlugin plugin, UUID id, String symbol, ScreenPositionalInfo positionalInfo) {
        super(id, new StockTickerFetcher(plugin, symbol), positionalInfo);

        this.symbol = symbol;

        // start renderer update task
        new TickerScreenUpdateTask(plugin, this).start();
    }


    /**
     * Fetches stock data
     */
    @Override
    public StockTicker fetch() {
        return this.fetcher.fetch();
    }

    /**
     * Returns the symbol
     *
     * @return symbol
     */
    public String getSymbol() {
        return symbol;
    }
}
