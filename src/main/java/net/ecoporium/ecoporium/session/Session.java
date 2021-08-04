package net.ecoporium.ecoporium.session;

import java.util.UUID;

public abstract class Session<T> {

    /**
     * The uuid in the session
     */
    private final UUID uuid;

    /**
     * Session
     *
     * @param uuid uuid
     */
    protected Session(UUID uuid) {
        this.uuid = uuid;
    }

    /**
     * Is the session complete?
     *
     * @return complete
     */
    public abstract boolean isComplete();
}
