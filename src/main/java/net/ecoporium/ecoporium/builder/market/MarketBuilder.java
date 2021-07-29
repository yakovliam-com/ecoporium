package net.ecoporium.ecoporium.builder.market;

import net.ecoporium.ecoporium.api.config.generic.adapter.ConfigurationAdapter;
import net.ecoporium.ecoporium.api.wrapper.Pair;
import net.ecoporium.ecoporium.builder.ConfigurationContextBuilder;
import net.ecoporium.ecoporium.model.market.GenericMarket;
import net.ecoporium.ecoporium.model.market.Market;
import net.ecoporium.ecoporium.model.market.MarketWhitelistOptions;

/**
 * Builds a market
 * {@code Pair<String, String>} = the handle and the current path
 * {@code ConfigurationAdapter} = the current config adapter context
 * {@code Market} = the market
 */
public class MarketBuilder extends ConfigurationContextBuilder<Pair<String, String>, Market> {

    private final MarketWhitelistOptionsBuilder marketWhitelistOptionsBuilder;

    public MarketBuilder() {
        this.marketWhitelistOptionsBuilder = new MarketWhitelistOptionsBuilder();
    }

    @Override
    public Market build(Pair<String, String> stringStringPair, ConfigurationAdapter configurationAdapter) {
        MarketWhitelistOptions options = marketWhitelistOptionsBuilder.build(stringStringPair.getRight(), configurationAdapter);

        return new GenericMarket(stringStringPair.getLeft(), options);
    }
}
