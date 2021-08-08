package net.ecoporium.ecoporium.market.factory;

import net.ecoporium.ecoporium.market.RealMarket;
import net.ecoporium.ecoporium.model.factory.Factory;

import java.util.HashMap;

public class RealMarketFactory implements Factory<String, RealMarket> {

    /**
     * Builds a V from K context
     *
     * @param context context
     * @return v
     */
    @Override
    public RealMarket build(String context) {
        return new RealMarket(context, new HashMap<>());
    }
}
