package net.ecoporium.ecoporium.storage.implementation.json.serializer.stock;

import net.ecoporium.ecoporium.market.stock.FakeStockProvider;
import net.ecoporium.ecoporium.market.stock.FakeStockTicker;
import net.ecoporium.ecoporium.market.stock.RealStockTicker;
import net.ecoporium.ecoporium.market.stock.StockTicker;
import net.ecoporium.ecoporium.market.stock.StockType;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;
import java.util.List;

public class StockTickerSerializer implements TypeSerializer<StockTicker> {

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
    public StockTicker deserialize(Type type, ConfigurationNode node) throws SerializationException {
        StockType stockType = node.node("type").get(StockType.class);
        String symbol = node.node("symbol").getString();

        if (stockType == StockType.FAKE) {
            List<String> aliases = node.node("aliases").getList(String.class);
            float previousClosingPrice = node.node("previousClosingPrice").getFloat();

            return new FakeStockTicker(symbol, aliases, new FakeStockProvider(previousClosingPrice));
        } else if (stockType == StockType.REAL) {
            return new RealStockTicker(symbol);
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
            FakeStockTicker fakeStockTicker = (FakeStockTicker) obj;

            node.node("aliases").set(fakeStockTicker.getAliases());
            node.node("previousClosingPrice").set(fakeStockTicker.getPrice());
        }
    }
}
