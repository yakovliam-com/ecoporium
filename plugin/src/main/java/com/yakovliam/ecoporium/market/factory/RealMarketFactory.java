package com.yakovliam.ecoporium.market.factory;

import com.yakovliam.ecoporium.api.model.factory.Factory;
import com.yakovliam.ecoporium.market.RealMarket;

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
