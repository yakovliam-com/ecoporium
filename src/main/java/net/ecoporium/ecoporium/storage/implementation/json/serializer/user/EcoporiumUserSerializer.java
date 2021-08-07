package net.ecoporium.ecoporium.storage.implementation.json.serializer.user;

import net.ecoporium.ecoporium.user.EcoporiumUser;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;

public class EcoporiumUserSerializer implements TypeSerializer<EcoporiumUser> {

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
    public EcoporiumUser deserialize(Type type, ConfigurationNode node) throws SerializationException {
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
    public void serialize(Type type, @Nullable EcoporiumUser obj, ConfigurationNode node) throws SerializationException {

    }
}
