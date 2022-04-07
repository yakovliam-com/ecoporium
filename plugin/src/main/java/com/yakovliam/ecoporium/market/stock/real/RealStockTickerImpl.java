package com.yakovliam.ecoporium.market.stock.real;

import com.yakovliam.ecoporium.api.market.stock.quote.SimpleStockQuote;
import com.yakovliam.ecoporium.api.market.stock.real.RealStockTicker;
import com.yakovliam.ecoporium.quotes.HistQuotes2Request;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.quotes.stock.StockQuote;

import java.io.IOException;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class RealStockTickerImpl extends RealStockTicker {

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
    private final TreeMap<Date, StockQuote> yfHistory;

    /**
     * Previous history, of past days
     */
    private final Map<Calendar, HistoricalQuote> yfPreviousHistory;

    /**
     * Stock ticker
     *
     * @param symbol symbol
     */
    public RealStockTickerImpl(String symbol) {
        super(symbol, Collections.emptyList(), null);
        this.symbol = symbol;
        this.stock = null;
        this.yfHistory = new TreeMap<>();
        this.yfPreviousHistory = new HashMap<>();
    }

    /**
     * Updates the stock's realtime data
     */
    @Override
    public void update() {
        updateStockData(false).thenAccept(data -> this.currentQuote = Optional.of(new SimpleStockQuote(stock.getQuote().getPrice().floatValue(), Date.from(Instant.now()))));
    }

    /**
     * Returns recent/short-term the history
     *
     * @return history
     */
    @Override
    public Map<Date, StockQuote> getYfHistory() {
        if (this.yfHistory.size() <= 0) {
            this.updateStockData(false).join();
        }

        return yfHistory;
    }

    /**
     * Returns the previous history
     *
     * @return history
     */
    @Override
    public Map<Calendar, HistoricalQuote> getYfPreviousHistory() {
        return yfPreviousHistory;
    }

    /**
     * Updates a ticker
     *
     * @return void completable future
     */
    @Override
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
        Date date = Date.from(Instant.now());
        this.yfHistory.put(date, recent.getQuote());
        this.history.add(new SimpleStockQuote(recent.getQuote().getPrice().floatValue(), date));

        // if the size is over the predetermined max, then pop the first
        if (this.yfHistory.size() > MAX_HISTORY_SIZE_BEFORE_POP) {
            this.yfHistory.pollFirstEntry();
        }

        // if the size is over the predetermined max, then pop the first
        if (this.history.size() > MAX_HISTORY_SIZE_BEFORE_POP) {
            this.history.pop();
        }
    }

    /**
     * Update previous history
     */
    private void updatePreviousHistory() {
        try {
            new HistQuotes2Request(this.symbol).result()
                    .stream()
                    .filter(Objects::nonNull)
                    .forEach(quote -> this.yfPreviousHistory.put(quote.getDate(), quote));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
