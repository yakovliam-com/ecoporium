package net.ecoporium.ecoporium.session;

import net.ecoporium.ecoporium.EcoporiumPlugin;

import java.util.Map;
import java.util.UUID;

public abstract class SessionManager<T> {

    /**
     * Session map
     */
    private final Map<UUID, T> sessionMap;

    /**
     * Ecoporium plugin
     */
    protected final EcoporiumPlugin plugin;

    /**
     * Session manager
     *
     * @param plugin     plugin
     * @param sessionMap map
     */
    protected SessionManager(EcoporiumPlugin plugin, Map<UUID, T> sessionMap) {
        this.plugin = plugin;
        this.sessionMap = sessionMap;
    }

    /**
     * Adds to the session map
     *
     * @param uuid    uuid
     * @param session session
     */
    protected void addToSessionMap(UUID uuid, T session) {
        this.sessionMap.put(uuid, session);
    }

    /**
     * Removes a uuid from a session map
     *
     * @param uuid uuid
     */
    protected void removeFromSessionMap(UUID uuid) {
        this.sessionMap.remove(uuid);
    }

    /**
     * Updates the session map
     *
     * @param uuid    uuid
     * @param session session
     */
    protected void updateSessionMap(UUID uuid, T session) {
        this.sessionMap.put(uuid, session);
    }

    /**
     * Returns a session
     *
     * @param uuid uuid
     * @return session
     */
    public T getSession(UUID uuid) {
        return this.sessionMap.getOrDefault(uuid, null);
    }

    /**
     * Returns the session map
     *
     * @return session map
     */
    protected Map<UUID, T> getSessionMap() {
        return sessionMap;
    }
}
