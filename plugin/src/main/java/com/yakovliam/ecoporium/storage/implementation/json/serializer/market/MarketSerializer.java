package com.yakovliam.ecoporium.storage.implementation.json.serializer.market;

import com.yakovliam.ecoporium.market.FakeMarket;
import com.yakovliam.ecoporium.market.Market;
import com.yakovliam.ecoporium.market.MarketType;
import com.yakovliam.ecoporium.market.RealMarket;
import com.yakovliam.ecoporium.market.stock.StockTicker;
import com.yakovliam.ecoporium.market.stock.fake.FakeStockTicker;
import com.yakovliam.ecoporium.market.stock.real.RealStockTicker;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MarketSerializer implements TypeSerializer<Market<?>> {

    /**
     * Instance
     */
    private static MarketSerializer instance;

    /**
     * Returns instance
     *
     * @return instance
     */
    public static MarketSerializer getInstance() {
        if (instance == null) {
            instance = new MarketSerializer();
        }

        return instance;
    }

    /**
     * Deserialize an object (of the correct type) from the given configuration
     * node.
     *
     * @param type the type of return value required
     * @param node the node containing serialized data
     * @return an object
     * @throws SerializationException if the presented data is invalid
     * @since 4.0.0
     */
    @Override
    public Market<?> deserialize(Type type, ConfigurationNode node) throws SerializationException {
        String handle = node.node("handle").getString();
        MarketType marketType = node.node("type").get(MarketType.class);

        // deserialize ticker cache
        @SuppressWarnings("rawtypes") List<StockTicker> tickers = node.node("stocks").getList(StockTicker.class);

        // type of market
        if (marketType == MarketType.FAKE) {
            List<FakeStockTicker> fakeStockTickers = Objects.requireNonNull(tickers).stream()
                    .map(t -> (FakeStockTicker) t)
                    .collect(Collectors.toList());

            Map<String, FakeStockTicker> tickerCache = fakeStockTickers.stream()
                    .collect(Collectors.toMap(StockTicker::getSymbol, Function.identity()));

            return new FakeMarket(handle, tickerCache);
        } else if (marketType == MarketType.REAL) {
            List<RealStockTicker> realStockTickers = Objects.requireNonNull(tickers).stream()
                    .map(t -> (RealStockTicker) t)
                    .collect(Collectors.toList());

            Map<String, RealStockTicker> tickerCache = realStockTickers.stream()
                    .collect(Collectors.toMap(StockTicker::getSymbol, Function.identity()));

            return new RealMarket(handle, tickerCache);
        }

        return null;
    }

    /**
     * Serialize an object to the given configuration node.
     *
     * @param type the type of the input object
     * @param obj  the object to be serialized
     * @param node the node to write to
     * @throws SerializationException if the object cannot be serialized
     * @since 4.0.0
     */
    @Override
    public void serialize(Type type, @Nullable Market obj, ConfigurationNode node) throws SerializationException {
        if (obj == null) {
            return;
        }

        node.node("handle").set(obj.getHandle());
        node.node("type").set(obj.getMarketType().name());

        @SuppressWarnings({"unchecked", "rawtypes"}) List<StockTicker> tickers = (List<StockTicker>) obj.getTickerCache().values().stream()
                .collect(Collectors.toList());
        node.node("stocks").setList(StockTicker.class, tickers);
    }
}
