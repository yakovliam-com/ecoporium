package net.ecoporium.ecoporium.ticker.fetch;

import net.ecoporium.ecoporium.EcoporiumPlugin;
import net.ecoporium.ecoporium.model.market.Market;
import net.ecoporium.ecoporium.model.market.StockTicker;

public class StockTickerFetcher {

    /**
     * Stock ticker symbol
     */
    private final String symbol;

    /**
     * Market
     */
    private final Market market;

    /**
     * Stock ticker fetcher
     *
     * @param plugin plugin
     * @param symbol symbol
     */
    public StockTickerFetcher(EcoporiumPlugin plugin, String symbol) {
        this.symbol = symbol;

        // find market by symbol
        market = plugin.getMarketCache().getMap().values().stream()
                .filter(m -> m.getWhitelistOptions().getTickers().contains(symbol) || m.getWhitelistOptions().getTickers().size() <= 0)
                .findFirst()
                .orElse(null);
    }

    /**
     * Fetches stock data
     *
     * @return data
     */
    public StockTicker fetch() {
        StockTicker ticker = market.getTicker(symbol);

        if (ticker == null) {
            ticker.fetchStockData().join();

            return ticker;
        }

        return ticker;
    }
}
