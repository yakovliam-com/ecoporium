package com.yakovliam.ecoporium.storage.implementation.json.serializer.user;

import com.google.common.collect.Table;
import com.yakovliam.ecoporium.api.user.share.OwnedShare;
import com.yakovliam.ecoporium.user.EcoporiumUserImpl;
import io.leangen.geantyref.TypeToken;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class EcoporiumUserSerializer implements TypeSerializer<EcoporiumUserImpl> {

    /**
     * Instance
     */
    private static EcoporiumUserSerializer instance;

    /**
     * Returns instance
     *
     * @return instance
     */
    public static EcoporiumUserSerializer getInstance() {
        if (instance == null) {
            instance = new EcoporiumUserSerializer();
        }

        return instance;
    }

    /**
     * Table type
     */
    private final TypeToken<Table<String, String, List<OwnedShare>>> tableType = new TypeToken<>() {
    };


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
    public EcoporiumUserImpl deserialize(Type type, ConfigurationNode node) throws SerializationException {
        UUID uuid = UUID.fromString(Objects.requireNonNull(node.node("uuid").getString()));
        Table<String, String, List<OwnedShare>> sharedOwnedTable = node.node("sharesOwnedTable").get(tableType);

        return new EcoporiumUserImpl(uuid, sharedOwnedTable);
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
    public void serialize(Type type, @Nullable EcoporiumUserImpl obj, ConfigurationNode node) throws SerializationException {
        node.node("uuid").set(Objects.requireNonNull(obj).uuid().toString());
        node.node("sharesOwnedTable").set(tableType, obj.sharesOwnedTable());
    }
}
