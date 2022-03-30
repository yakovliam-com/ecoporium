package com.yakovliam.ecoporium.api.market.stock.fake;

import com.yakovliam.ecoporium.api.market.stock.StockTicker;
import com.yakovliam.ecoporium.api.market.stock.StockType;

import java.util.List;

public abstract class FakeStockTicker extends StockTicker<FakeStockProvider> {

    /**
     * Stock
     *  @param symbol    symbol
     * @param aliases   aliases
     * @param stock     stock
     */
    protected FakeStockTicker(String symbol, List<String> aliases, FakeStockProvider stock) {
        super(symbol, aliases, stock, StockType.FAKE);
    }
}
