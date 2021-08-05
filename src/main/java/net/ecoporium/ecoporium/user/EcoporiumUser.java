package net.ecoporium.ecoporium.user;

import java.util.UUID;

public class EcoporiumUser {

    /**
     * The uuid of the associated player
     */
    private final UUID uuid;

    /**
     * Ecoporium user
     *
     * @param uuid uuid
     */
    public EcoporiumUser(UUID uuid) {
        this.uuid = uuid;
    }

    /**
     * Returns the uuid
     *
     * @return uuid
     */
    public UUID getUuid() {
        return uuid;
    }
}
