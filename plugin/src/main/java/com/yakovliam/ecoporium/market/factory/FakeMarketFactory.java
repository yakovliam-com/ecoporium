package com.yakovliam.ecoporium.market.factory;

import com.yakovliam.ecoporium.api.model.factory.Factory;
import com.yakovliam.ecoporium.market.FakeMarket;

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