package net.ecoporium.ecoporium.market.factory;

import net.ecoporium.ecoporium.market.FakeMarket;
import net.ecoporium.ecoporium.model.factory.Factory;

import java.util.HashMap;

public class FakeMarketFactory implements Factory<String, FakeMarket> {

    /**
     * Builds a V from K context
     *
     * @param context context
     * @return v
     */
    @Override
    public FakeMarket build(String context) {
        return new FakeMarket(context, new HashMap<>());
    }
}