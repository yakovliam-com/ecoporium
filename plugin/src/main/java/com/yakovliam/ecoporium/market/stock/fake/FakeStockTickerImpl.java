package com.yakovliam.ecoporium.market.stock.fake;

import com.yakovliam.ecoporium.api.market.stock.fake.FakeStockTicker;
import com.yakovliam.ecoporium.api.market.stock.quote.SimpleStockQuote;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class FakeStockTickerImpl extends FakeStockTicker {

    /**
     * Fake stock provider
     */
    private final FakeStockProviderImpl fakeStockProviderImpl;

    /**
     * Stock
     *
     * @param symbol  symbol
     * @param aliases aliases
     */
    public FakeStockTickerImpl(String symbol, List<String> aliases, FakeStockProviderImpl fakeStockProviderImpl) {
        super(symbol, aliases, fakeStockProviderImpl);
        this.fakeStockProviderImpl = fakeStockProviderImpl;
        this.currentQuote = Optional.of(new SimpleStockQuote(fakeStockProviderImpl.calculateOpeningPrice(), Date.from(Instant.now())));
    }

    /**
     * Updates the stock's realtime data
     */
    @Override
    public void update() {
        this.currentQuote = Optional.of(
                new SimpleStockQuote(
                        this.fakeStockProviderImpl.calculatePrice(this.currentQuote.orElse(
                                // this won't happen, but java
                                new SimpleStockQuote(0.0f, Date.from(Instant.now()))
                        ).getPrice()),
                        Date.from(Instant.now()))
        );
        updateHistory();
    }

    /**
     * Updates the stock history
     */
    private void updateHistory() {
        // update history
        this.currentQuote.ifPresent(this.history::add);

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
    public FakeStockProviderImpl getFakeStockProvider() {
        return fakeStockProviderImpl;
    }
}
