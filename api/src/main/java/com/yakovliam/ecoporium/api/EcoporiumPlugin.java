package com.yakovliam.ecoporium.api;

import com.yakovliam.ecoporium.api.market.MarketCache;
import com.yakovliam.ecoporium.api.user.EcoporiumUser;
import com.yakovliam.ecoporium.api.user.UserCache;

public abstract class EcoporiumPlugin extends Plugin {

    /**
     * Returns the market cache
     *
     * @return market cache
     */
    public abstract MarketCache getMarketCache();


    /**
     * Returns user cache
     *
     * @return user cache
     */
    public abstract UserCache<? extends EcoporiumUser> getUserCache();
}
