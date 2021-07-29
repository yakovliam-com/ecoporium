package net.ecoporium.ecoporium.model.market;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.quotes.stock.StockQuote;

import java.io.IOException;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class StockTicker {

    /**
     * The symbol for the current stock ticker
     */
    private final String symbol;

    /**
     * Stock data (contains the quote as well)
     */
    private Stock stock;

    /**
     * Current stock history
     * <p>
     * Data here only exists into the past since the stock was first queried
     */
    private final Map<Date, StockQuote> history;

    /**
     * Stock ticker
     *
     * @param symbol symbol
     */
    public StockTicker(String symbol) {
        this.symbol = symbol;
        this.stock = null;
        this.history = new HashMap<>();
    }

    /**
     * Returns the ticker symbol
     *
     * @return symbol
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * Returns the current stock data
     *
     * @return stock
     */
    public Stock getCurrentStockData() {
        return stock;
    }

    /**
     * Returns recent/short-term the history
     *
     * @return history
     */
    public Map<Date, StockQuote> getHistory() {
        return history;
    }

    /**
     * Returns a stock once it has gotten the values
     *
     * @return stock
     */
    public CompletableFuture<StockTicker> get() {
        if (stock != null) {
            return CompletableFuture.completedFuture(this);
        } else {
            return update();
        }
    }

    /**
     * Updates a ticker
     **/
    public CompletableFuture<StockTicker> update() {
        if (stock != null) {
            // do update
            return CompletableFuture.supplyAsync(() -> {
                try {
                    stock.getQuote(true);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // update history
                this.updateHistory(stock);

                return this;
            });
        } else {
            // get from api
            return CompletableFuture.supplyAsync(() -> {
                Stock stock = null;
                try {
                    stock = YahooFinance.get(symbol);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return stock;
            }).thenApply(stock -> {
                this.stock = stock;

                // update history
                this.updateHistory(stock);

                return this;
            });
        }
    }

    /**
     * Updates the stock history
     *
     * @param recent recent stock data
     */
    private void updateHistory(Stock recent) {
        this.history.put(Date.from(Instant.now()), recent.getQuote());
    }
}
