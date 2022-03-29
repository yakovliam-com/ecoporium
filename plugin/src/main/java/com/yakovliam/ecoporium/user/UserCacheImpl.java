package com.yakovliam.ecoporium.user;

import com.yakovliam.ecoporium.lib.caffeine.cache.Caffeine;
import com.yakovliam.ecoporium.EcoporiumPlugin;
import com.yakovliam.ecoporium.api.user.UserCache;

public class UserCacheImpl extends UserCache<EcoporiumUserImpl> {

    /**
     * Cache
     *
     * @param plugin plugin
     */
    public UserCacheImpl(EcoporiumPlugin plugin) {
        super(Caffeine.newBuilder()
                .buildAsync(u -> plugin.getStorage().loadUser(u)));
    }
}