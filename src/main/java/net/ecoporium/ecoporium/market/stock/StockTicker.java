package net.ecoporium.ecoporium.market.stock;

import java.util.List;

public abstract class StockTicker<T> {

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
}
