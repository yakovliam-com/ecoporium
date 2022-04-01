package com.yakovliam.ecoporium.storage.implementation.json.serializer.table;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yakovliam.ecoporium.api.user.share.OwnedShare;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TableSerializer implements TypeSerializer<Table<String, String, List<OwnedShare>>> {

    /**
     * Instance
     */
    private static TableSerializer instance;

    // gson
    private final Gson gson = new Gson();
    private final Type listType = new TypeToken<ArrayList<OwnedShare>>() {
    }.getType();

    /**
     * Returns instance
     *
     * @return instance
     */
    public static TableSerializer getInstance() {
        if (instance == null) {
            instance = new TableSerializer();
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
    public Table<String, String, List<OwnedShare>> deserialize(Type type, ConfigurationNode node) throws SerializationException {
        Table<String, String, List<OwnedShare>> table = HashBasedTable.create();
        List<String> cells = node.node("cells").getList(String.class);
        Objects.requireNonNull(cells).forEach(cell -> {
            String[] parts = cell.split("\\|");
            if (parts.length != 3) {
                return;
            }
            String rK = parts[0];
            String cK = parts[1];
            List<OwnedShare> v = gson.fromJson(parts[2], listType);

            table.put(rK, cK, v);
        });

        return table;
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
    public void serialize(Type type, @Nullable Table<String, String, List<OwnedShare>> obj, ConfigurationNode node) throws SerializationException {
        List<String> cells = new ArrayList<>();

        Objects.requireNonNull(obj).cellSet().forEach(cell -> {
            String computed = cell.getRowKey() + "|" + cell.getColumnKey() + "|" + gson.toJson(cell.getValue(), listType);
            cells.add(computed);
        });

        node.node("cells").setList(String.class, cells);
    }
}
