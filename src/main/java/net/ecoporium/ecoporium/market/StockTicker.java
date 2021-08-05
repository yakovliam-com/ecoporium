package net.ecoporium.ecoporium.market;

import net.ecoporium.ecoporium.quotes.HistQuotes2Request;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.quotes.stock.StockQuote;

import java.io.IOException;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;

public class StockTicker {

    /**
     * Maximum size before pop
     */
    private static final int MAX_HISTORY_SIZE_BEFORE_POP = 30;

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
    private final TreeMap<Date, StockQuote> history;

    /**
     * Previous history, of past days
     */
    private final Map<Calendar, HistoricalQuote> previousHistory;

    /**
     * Stock ticker
     *
     * @param symbol symbol
     */
    public StockTicker(String symbol) {
        this.symbol = symbol;
        this.stock = null;
        this.history = new TreeMap<>();
        this.previousHistory = new HashMap<>();
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
        if (this.history.size() <= 0) {
            this.updateStockData(false).join();
        }

        return history;
    }

    /**
     * Returns the previous history
     *
     * @return history
     */
    public Map<Calendar, HistoricalQuote> getPreviousHistory() {
        return previousHistory;
    }

    /**
     * Fetches all stock data
     * <p>
     * This should only be called once, and the rest of the times
     * one of the methods below that update specific types of data should be called instead
     *
     * @return void completable future
     */
    public CompletableFuture<Void> fetchStockData() {
        return CompletableFuture.supplyAsync(() -> {
            updateStockData(true).join();
            return null;
        });
    }

    /**
     * Updates only the live trend data
     *
     * @return void completable future
     */
    public CompletableFuture<Void> updateLiveTrendData() {
        return updateStockData(false);
    }

    /**
     * Updates a ticker
     *
     * @return void completable future
     */
    public CompletableFuture<Void> updateStockData(boolean previousHistory) {
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
                if (previousHistory) {
                    // update previous history
                    this.updatePreviousHistory();
                }

                return null;
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
                if (previousHistory) {
                    // update previous history
                    this.updatePreviousHistory();
                }

                return null;
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

        // if the size is over the predetermined max, then pop the first
        if (this.history.size() > MAX_HISTORY_SIZE_BEFORE_POP) {
            this.history.pollFirstEntry();
        }
    }

    /**
     * Update previous history
     */
    private void updatePreviousHistory() {
        try {
            new HistQuotes2Request(this.symbol).getResult()
                    .stream()
                    .filter(Objects::nonNull)
                    .forEach(quote -> this.previousHistory.put(quote.getDate(), quote));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
