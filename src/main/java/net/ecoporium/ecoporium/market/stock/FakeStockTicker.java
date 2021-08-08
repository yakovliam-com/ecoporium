package net.ecoporium.ecoporium.market.stock;

import java.util.List;

public class FakeStockTicker extends StockTicker<FakeStockProvider> {

    /**
     * Price
     */
    private float price;

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
        this.price = fakeStockProvider.calculateOpeningPrice();
    }

    /**
     * Returns the price
     *
     * @return price
     */
    public float getPrice() {
        return this.price;
    }

    /**
     * Updates the stock's realtime data
     */
    @Override
    public void update() {
        this.price = this.fakeStockProvider.calculatePrice(getPrice());
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
