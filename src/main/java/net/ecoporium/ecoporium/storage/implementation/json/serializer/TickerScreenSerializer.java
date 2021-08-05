package net.ecoporium.ecoporium.storage.implementation.json.serializer;

import net.ecoporium.ecoporium.EcoporiumPlugin;
import net.ecoporium.ecoporium.market.Market;
import net.ecoporium.ecoporium.screen.StaticTickerScreen;
import net.ecoporium.ecoporium.screen.TickerScreen;
import net.ecoporium.ecoporium.screen.data.TickerScreenInfo;
import net.ecoporium.ecoporium.screen.data.TickerScreenMapData;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class TickerScreenSerializer implements TypeSerializer<TickerScreen> {

    /**
     * Ecoporium plugin
     */
    private final EcoporiumPlugin plugin;

    /**
     * Ticker screen serializer
     *
     * @param plugin plugin
     */
    public TickerScreenSerializer(EcoporiumPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public TickerScreen deserialize(Type type, ConfigurationNode node) throws SerializationException {
        // get id
        UUID screenId = UUID.fromString(Objects.requireNonNull(node.node("id").getString()));
        // get map ids
        List<Integer> mapIds = node.node("mapIds").getList(Integer.class);
        // get w/h
        int width = node.node("width").getInt();
        int height = node.node("height").getInt();

        // if type
        String typeString = node.node("type").getString();

        if (typeString.equalsIgnoreCase("static")) {

            String symbol = node.node("symbol").getString();
            String marketHandle = node.node("market").getString();

            // get market
            Market market = plugin.getMarketCache().get(marketHandle, null);

            StaticTickerScreen tickerScreen = new StaticTickerScreen(screenId, market, symbol, new TickerScreenInfo(width, height));
            tickerScreen.setTickerScreenMapData(new TickerScreenMapData(mapIds));

            return tickerScreen;
        }

        return null;
    }

    @Override
    public void serialize(Type type, @Nullable TickerScreen obj, ConfigurationNode node) throws SerializationException {
        // get screen id
        UUID screenId = obj.getId();
        // get map ids
        List<Integer> mapIds = obj.getTickerScreenMapData().getMapIds();

        // get w/h
        int width = obj.getTickerScreenInfo().getWidth();
        int height = obj.getTickerScreenInfo().getHeight();

        // set
        node.node("id").set(screenId.toString());
        node.node("mapIds").set(mapIds);
        node.node("width").set(width);
        node.node("height").set(height);

        // if static
        if (obj instanceof StaticTickerScreen) {
            StaticTickerScreen staticTickerScreen = (StaticTickerScreen) obj;

            node.node("type").set("static");
            node.node("market").set(staticTickerScreen.getMarket().getHandle());
            node.node("symbol").set(staticTickerScreen.getSymbol());

        } else {
            node.node("type").set("unknown");
        }
    }
}
