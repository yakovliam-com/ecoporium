package com.yakovliam.ecoporium.market.stock.fake;

import com.yakovliam.ecoporium.market.stock.StockTicker;
import com.yakovliam.ecoporium.market.stock.StockType;
import com.yakovliam.ecoporium.market.stock.quote.SimpleStockQuote;

import java.time.Instant;
import java.util.Date;
import java.util.List;

public class FakeStockTicker extends StockTicker<FakeStockProvider> {

    /**
     * Fake stock provider
     */
    private final FakeStockProvider fakeStockProvider;

    /**
     * Stock
     *
     * @param symbol  symbol
     * @param aliases aliases
     */
    public FakeStockTicker(String symbol, List<String> aliases, FakeStockProvider fakeStockProvider) {
        super(symbol, aliases, fakeStockProvider, StockType.FAKE);
        this.fakeStockProvider = fakeStockProvider;
        this.currentQuote = new SimpleStockQuote(fakeStockProvider.calculateOpeningPrice(), Date.from(Instant.now()));
    }

    /**
     * Updates the stock's realtime data
     */
    @Override
    public void update() {
        this.currentQuote = new SimpleStockQuote(this.fakeStockProvider.calculatePrice(this.currentQuote.getPrice()), Date.from(Instant.now()));
        updateHistory();
    }

    /**
     * Updates the stock history
     */
    private void updateHistory() {
        // update history
        this.history.add(this.currentQuote);

        // if the size is over the predetermined max, then pop the first
        if (this.history.size() > MAX_HISTORY_SIZE_BEFORE_POP) {
            this.history.pop();
        }
    }


    /**
     * Returns the fake stock provider
     *
     * @return stock provider
     */
    public FakeStockProvider getFakeStockProvider() {
        return fakeStockProvider;
    }
}
