package net.ecoporium.ecoporium.session.screen;

import net.ecoporium.ecoporium.EcoporiumPlugin;
import net.ecoporium.ecoporium.session.SessionManager;
import net.ecoporium.ecoporium.ticker.StaticTickerScreen;

import java.util.HashMap;
import java.util.UUID;

public class TickerScreenCreatorSessionManager extends SessionManager<TickerScreenCreatorSession> {

    /**
     * Session manager
     *
     * @param plugin     plugin
     */
    public TickerScreenCreatorSessionManager(EcoporiumPlugin plugin) {
        super(plugin, new HashMap<>());
    }

    /**
     * Creates a session
     *
     * @param uuid uuid
     */
    public TickerScreenCreatorSession createSession(UUID uuid) {
        // create a new session
        TickerScreenCreatorSession screenCreatorSession = new TickerScreenCreatorSession(uuid);

        // add to map
        addToSessionMap(uuid, screenCreatorSession);

        // return session
        return screenCreatorSession;
    }

    /**
     * Removes a session
     *
     * @param uuid uuid
     */
    public void removeSession(UUID uuid) {
        // add to map
        removeFromSessionMap(uuid);
    }

    /**
     * Updates the session map
     *
     * @param uuid                 uuid
     * @param screenCreatorSession screen creator session
     * @return updated
     */
    public TickerScreenCreatorSession updateSession(UUID uuid, TickerScreenCreatorSession screenCreatorSession) {
        // update
        updateSessionMap(uuid, screenCreatorSession);

        // return
        return screenCreatorSession;
    }

    /**
     * Creates a static ticker screen
     *
     * @param uuid   uuid
     * @param symbol symbol
     * @return ticker screen
     */
    public StaticTickerScreen createStaticTickerScreen(UUID uuid, String symbol) {
        TickerScreenCreatorSession screenCreatorSession = this.getSessionMap().getOrDefault(uuid, createSession(uuid));
        return new StaticTickerScreen(plugin, UUID.randomUUID(), symbol, screenCreatorSession.getScreenPositionalInfo());
    }
}
