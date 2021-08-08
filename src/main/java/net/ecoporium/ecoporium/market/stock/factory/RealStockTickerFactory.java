package net.ecoporium.ecoporium.market.stock.factory;

import net.ecoporium.ecoporium.market.stock.RealStockTicker;
import net.ecoporium.ecoporium.model.factory.Factory;

public class RealStockTickerFactory implements Factory<String, RealStockTicker> {

    /**
     * Builds a V from K context
     *
     * @param context context
     * @return v
     */
    @Override
    public RealStockTicker build(String context) {
        return new RealStockTicker(context);
    }
}
