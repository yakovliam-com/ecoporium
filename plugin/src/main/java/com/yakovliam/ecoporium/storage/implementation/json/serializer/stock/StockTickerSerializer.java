package com.yakovliam.ecoporium.storage.implementation.json.serializer.stock;

import com.yakovliam.ecoporium.api.market.stock.StockTicker;
import com.yakovliam.ecoporium.api.market.stock.StockType;
import com.yakovliam.ecoporium.market.stock.fake.FakeStockProviderImpl;
import com.yakovliam.ecoporium.market.stock.fake.FakeStockTickerImpl;
import com.yakovliam.ecoporium.market.stock.real.RealStockTickerImpl;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;
import java.util.List;

public class StockTickerSerializer implements TypeSerializer<StockTicker<?>> {

    /**
     * Instance
     */
    private static StockTickerSerializer instance;

    /**
     * Returns instance
     *
     * @return instance
     */
    public static StockTickerSerializer getInstance() {
        if (instance == null) {
            instance = new StockTickerSerializer();
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
    public StockTicker<?> deserialize(Type type, ConfigurationNode node) throws SerializationException {
        StockType stockType = node.node("type").get(StockType.class);
        String symbol = node.node("symbol").getString();

        if (stockType == StockType.FAKE) {
            List<String> aliases = node.node("aliases").getList(String.class);
            float previousClosingPrice = node.node("previousClosingPrice").getFloat();

            return new FakeStockTickerImpl(symbol, aliases, new FakeStockProviderImpl(previousClosingPrice));
        } else if (stockType == StockType.REAL) {
            return new RealStockTickerImpl(symbol);
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
    public void serialize(Type type, @Nullable StockTicker obj, ConfigurationNode node) throws SerializationException {
        if (obj == null) {
            return;
        }

        node.node("type").set(obj.getStockType());
        node.node("symbol").set(obj.getSymbol());

        if (obj.getStockType() == StockType.FAKE) {
            FakeStockTickerImpl fakeStockTickerImpl = (FakeStockTickerImpl) obj;

            node.node("aliases").set(fakeStockTickerImpl.getAliases());
            node.node("previousClosingPrice").set(fakeStockTickerImpl.getCurrentQuote().getPrice());
        }
    }
}
