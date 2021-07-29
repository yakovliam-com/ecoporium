package net.ecoporium.ecoporium.model.market;

import com.github.benmanes.caffeine.cache.Caffeine;
import net.ecoporium.ecoporium.model.cache.AsyncCache;
import yahoofinance.YahooFinance;

import java.util.concurrent.TimeUnit;

public class GenericMarket extends Market {

    /**
     * Market
     *
     * @param handle           handle
     * @param whitelistOptions whitelist options
     */
    public GenericMarket(String handle, MarketWhitelistOptions whitelistOptions) {
        super(handle, whitelistOptions, new AsyncCache<>(Caffeine.newBuilder()
                .expireAfterWrite(1, TimeUnit.MINUTES)
                .buildAsync(YahooFinance::get)));
    }
}
