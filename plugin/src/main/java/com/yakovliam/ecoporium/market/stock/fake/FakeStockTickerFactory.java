package com.yakovliam.ecoporium.market.stock.fake;

import com.yakovliam.ecoporium.api.model.factory.Factory;
import com.yakovliam.ecoporium.api.wrapper.Pair;

import java.util.Collections;

public class FakeStockTickerFactory implements Factory<Pair<String, String>, FakeStockTickerImpl> {

    /**
     * Builds a V from K context
     *
     * @param context context
     * @return v
     */
    @Override
    public FakeStockTickerImpl build(Pair<String, String> context) {
        return new FakeStockTickerImpl(context.left(), context.right() == null ? Collections.emptyList() : Collections.singletonList(context.right()), new FakeStockProviderImpl());
    }
}
