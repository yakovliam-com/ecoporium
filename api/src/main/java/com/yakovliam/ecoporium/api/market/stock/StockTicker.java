package com.yakovliam.ecoporium.api.market.stock;

import com.yakovliam.ecoporium.api.market.stock.quote.SimpleStockQuote;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.LinkedBlockingDeque;

public abstract class StockTicker<T> {

    /**
     * Maximum size before pop
     */
    protected static final int MAX_HISTORY_SIZE_BEFORE_POP = 30;

    /**
     * Symbol
     */
    private final String symbol;

    /**
     * Aliases
     */
    private final List<String> aliases;

    /**
     * The stock object
     */
    private final T stock;

    /**
     * Stock type
     */
    private final StockType stockType;

    /**
     * The current stock quote
     */
    protected Optional<SimpleStockQuote> currentQuote;

    /**
     * The local quote history (since instantiation)
     */
    protected final LinkedBlockingDeque<SimpleStockQuote> history;

    /**
     * Stock
     *
     * @param symbol    symbol
     * @param aliases   aliases
     * @param stock     stock
     * @param stockType type
     */
    protected StockTicker(String symbol, List<String> aliases, T stock, StockType stockType) {
        this.symbol = symbol;
        this.aliases = aliases;
        this.stock = stock;
        this.stockType = stockType;
        this.history = new LinkedBlockingDeque<>();
        this.currentQuote = Optional.empty();
    }

    /**
     * Returns symbol
     *
     * @return symbol
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * Returns aliases
     *
     * @return aliases
     */
    public List<String> getAliases() {
        return aliases;
    }

    /**
     * Returns the stock
     *
     * @return stock
     */
    public T getStock() {
        return stock;
    }

    /**
     * Updates the stock's realtime data
     */
    public abstract void update();

    /**
     * Returns the stock type
     *
     * @return type
     */
    public StockType getStockType() {
        return stockType;
    }

    /**
     * Returns the current quote
     *
     * @return quote
     */
    public Optional<SimpleStockQuote> getCurrentQuote() {
        return currentQuote;
    }

    /**
     * Returns the stock's history
     *
     * @return history
     */
    public LinkedBlockingDeque<SimpleStockQuote> getHistory() {
        return this.history;
    }

    /**
     * Returns the historical analysis of the current data
     *
     * @return hist analysis
     */
    public HistoricalAnalysis getHistoricalAnalysis() {
        if (this.history.size() <= 1) {
            return HistoricalAnalysis.NOT_APPLICABLE;
        }

        LinkedBlockingDeque<SimpleStockQuote> historyCopy = new LinkedBlockingDeque<>(history);

        // get last two
        float lastPrice = 0;
        float secondToLastPrice = 0;
        try {
            lastPrice = historyCopy.takeLast().price();
            secondToLastPrice = historyCopy.takeLast().price();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // analyze
        if (lastPrice > secondToLastPrice) {
            return HistoricalAnalysis.GOING_UP;
        } else {
            return HistoricalAnalysis.GOING_DOWN;
        }
    }
}
