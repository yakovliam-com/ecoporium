package net.ecoporium.ecoporium.market.stock.real;

import net.ecoporium.ecoporium.api.model.factory.Factory;

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
