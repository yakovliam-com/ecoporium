package com.yakovliam.ecoporium.market.factory;

import com.yakovliam.ecoporium.api.model.factory.Factory;
import com.yakovliam.ecoporium.market.FakeMarketImpl;

import java.util.HashMap;

public class FakeMarketFactory implements Factory<String, FakeMarketImpl> {

    /**
     * Builds a V from K context
     *
     * @param context context
     * @return v
     */
    @Override
    public FakeMarketImpl build(String context) {
        return new FakeMarketImpl(context, new HashMap<>());
    }
}