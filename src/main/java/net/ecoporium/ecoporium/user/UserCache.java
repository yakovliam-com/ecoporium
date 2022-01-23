package net.ecoporium.ecoporium.user;

import com.github.benmanes.caffeine.cache.Caffeine;
import net.ecoporium.ecoporium.EcoporiumPlugin;
import net.ecoporium.ecoporium.api.model.cache.AsyncCache;

import java.util.UUID;

public class UserCache extends AsyncCache<UUID, EcoporiumUser> {

    /**
     * Cache
     *
     * @param plugin plugin
     */
    public UserCache(EcoporiumPlugin plugin) {
        super(Caffeine.newBuilder()
                .buildAsync(u -> plugin.getStorage().loadUser(u)));
    }
}
