package net.ecoporium.ecoporium.screen.fetch;

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
     * @param symbol symbol
     */
    public StockTickerFetcher(Market market, String symbol) {
        this.symbol = symbol;
        this.market = market;
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
