package net.ecoporium.ecoporium.market.stock.factory;

import net.ecoporium.ecoporium.api.wrapper.Pair;
import net.ecoporium.ecoporium.market.stock.FakeStockProvider;
import net.ecoporium.ecoporium.market.stock.FakeStockTicker;
import net.ecoporium.ecoporium.model.factory.Factory;

import java.util.Collections;

public class FakeStockTickerFactory implements Factory<Pair<String, String>, FakeStockTicker> {

    /**
     * Builds a V from K context
     *
     * @param context context
     * @return v
     */
    @Override
    public FakeStockTicker build(Pair<String, String> context) {
        return new FakeStockTicker(context.getLeft(), context.getRight() == null ? Collections.emptyList() : Collections.singletonList(context.getRight()), new FakeStockProvider());
    }
}
