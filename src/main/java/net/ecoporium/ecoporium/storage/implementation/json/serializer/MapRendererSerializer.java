package net.ecoporium.ecoporium.storage.implementation.json.serializer;

import com.google.gson.Gson;
import org.bukkit.map.MapRenderer;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;

public class MapRendererSerializer implements TypeSerializer<MapRendererWrapper> {

    private final Gson gson = new Gson();

    @Override
    public MapRendererWrapper deserialize(Type type, ConfigurationNode node) throws SerializationException {
        int id = node.node("id").getInt();
        MapRenderer renderer = gson.fromJson(node.node("renderer").getString(), MapRenderer.class);
        return new MapRendererWrapper(id, renderer);
    }

    @Override
    public void serialize(Type type, @Nullable MapRendererWrapper obj, ConfigurationNode node) throws SerializationException {
        node.node("id").set(Integer.class, obj.getId());
        node.node("renderer").set(gson.toJson(obj.getMapRenderer(), MapRenderer.class));
    }
}
