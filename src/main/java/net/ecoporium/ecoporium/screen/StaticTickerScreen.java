package net.ecoporium.ecoporium.screen;

import net.ecoporium.ecoporium.EcoporiumPlugin;
import net.ecoporium.ecoporium.market.Market;
import net.ecoporium.ecoporium.market.StockTicker;
import net.ecoporium.ecoporium.screen.fetch.StockTickerFetcher;
import net.ecoporium.ecoporium.screen.data.TickerScreenInfo;
import net.ecoporium.ecoporium.screen.task.TickerScreenUpdateTask;

import java.util.UUID;

public class StaticTickerScreen extends TickerScreen {

    /**
     * Symbol
     */
    private final String symbol;

    /**
     * Market
     */
    private final Market market;

    /**
     * Update task
     */
    private TickerScreenUpdateTask tickerScreenUpdateTask;

    /**
     * Static ticker screen
     * <p>
     * Once initialized, this screen will display the applicable symbol's data, and that data only
     *
     * @param id               id
     * @param market           market
     * @param symbol           symbol
     * @param tickerScreenInfo screenInfo
     */
    public StaticTickerScreen(UUID id, Market market, String symbol, TickerScreenInfo tickerScreenInfo) {
        super(id, new StockTickerFetcher(market, symbol), tickerScreenInfo);

        this.market = market;
        this.symbol = symbol;
    }

    /**
     * Fetches stock data
     */
    @Override
    public StockTicker fetch() {
        return this.fetcher.fetch();
    }

    @Override
    public void cancel() {
        if (this.tickerScreenUpdateTask != null) {
            this.tickerScreenUpdateTask.stop();
        }
    }

    @Override
    public void start(EcoporiumPlugin plugin) {
        // start renderer update task
        this.tickerScreenUpdateTask = new TickerScreenUpdateTask(plugin, this);
        tickerScreenUpdateTask.start();
    }

    /**
     * Returns the symbol
     *
     * @return symbol
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * Returns market
     *
     * @return market
     */
    public Market getMarket() {
        return market;
    }
}
