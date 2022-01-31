package com.yakovliam.ecoporium.market.factory;

import com.yakovliam.ecoporium.api.model.factory.Factory;
import com.yakovliam.ecoporium.market.RealMarketImpl;

import java.util.HashMap;

public class RealMarketFactory implements Factory<String, RealMarketImpl> {

    /**
     * Builds a V from K context
     *
     * @param context context
     * @return v
     */
    @Override
    public RealMarketImpl build(String context) {
        return new RealMarketImpl(context, new HashMap<>());
    }
}
