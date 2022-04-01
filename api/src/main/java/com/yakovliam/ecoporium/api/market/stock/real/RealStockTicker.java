package com.yakovliam.ecoporium.api.market.stock.real;

import com.yakovliam.ecoporium.api.market.stock.StockTicker;
import com.yakovliam.ecoporium.api.market.stock.StockType;
import yahoofinance.Stock;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.quotes.stock.StockQuote;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public abstract class RealStockTicker extends StockTicker<Stock> {

    /**
     * Stock
     *  @param symbol    symbol
     * @param aliases   aliases
     * @param stock     stock
     */
    protected RealStockTicker(String symbol, List<String> aliases, Stock stock) {
        super(symbol, aliases, stock, StockType.REAL);
    }

    /**
     * Returns recent/short-term the history
     *
     * @return history
     */
    public abstract Map<Date, StockQuote> getYfHistory();

    /**
     * Returns the previous history
     *
     * @return history
     */
    public abstract Map<Calendar, HistoricalQuote> getYfPreviousHistory();

    /**
     * Updates a ticker
     *
     * @return void completable future
     */
    public abstract CompletableFuture<Void> updateStockData(boolean previousHistory);
}
