package net.ecoporium.ecoporium.storage.implementation.json.serializer.screen;

import net.ecoporium.ecoporium.EcoporiumPlugin;
import net.ecoporium.ecoporium.market.Market;
import net.ecoporium.ecoporium.market.stock.StockTicker;
import net.ecoporium.ecoporium.screen.TrendScreen;
import net.ecoporium.ecoporium.screen.info.MapInfo;
import net.ecoporium.ecoporium.screen.info.ScreenInfo;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;
import java.util.Objects;
import java.util.UUID;

public class TrendScreenSerializer implements TypeSerializer<TrendScreen> {

    /**
     * Instance
     */
    private static TrendScreenSerializer instance;

    /**
     * Returns instance
     *
     * @return instance
     */
    public static TrendScreenSerializer getInstance(EcoporiumPlugin context) {
        if (instance == null) {
            instance = new TrendScreenSerializer(context);
        }

        return instance;
    }

    /**
     * Ecoporium plugin
     */
    private final EcoporiumPlugin plugin;

    /**
     * Trend screen serializer
     *
     * @param plugin plugin
     */
    public TrendScreenSerializer(EcoporiumPlugin plugin) {
        this.plugin = plugin;
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
    public TrendScreen deserialize(Type type, ConfigurationNode node) throws SerializationException {
        UUID uuid = UUID.fromString(Objects.requireNonNull(node.node("uuid").getString()));
        // get market from handle
        String marketHandle = node.node("market").getString();
        Market<?> market = plugin.getMarketCache().getCache().get(marketHandle).join();

        if (market == null) {
            return null;
        }

        StockTicker<?> stockTicker = market.getStock(node.node("symbol").getString());

        if (stockTicker == null) {
            return null;
        }

        // construct screen info
        ScreenInfo screenInfo = new ScreenInfo(node.node("width").getInt(), node.node("height").getInt());
        // construct map info
        MapInfo mapInfo = new MapInfo(node.node("mapIds").getList(Integer.class));

        return new TrendScreen(uuid, market, stockTicker, screenInfo, mapInfo);
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
    public void serialize(Type type, @Nullable TrendScreen obj, ConfigurationNode node) throws SerializationException {
        // set uuid
        node.node("uuid").set(obj.getUuid().toString());

        // set market
        node.node("market").set(obj.getMarket().getHandle());

        // set stock ticker symbol
        node.node("symbol").set(obj.getTicker().getSymbol());

        // set width / height
        node.node("width").set(obj.getScreenInfo().getWidth());
        node.node("height").set(obj.getScreenInfo().getHeight());

        // set mapIds
        node.node("mapIds").setList(Integer.class, obj.getMapInfo().getMapIds());
    }
}
