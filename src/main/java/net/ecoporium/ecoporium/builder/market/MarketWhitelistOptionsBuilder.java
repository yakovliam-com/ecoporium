package net.ecoporium.ecoporium.builder.market;

import net.ecoporium.ecoporium.api.config.generic.adapter.ConfigurationAdapter;
import net.ecoporium.ecoporium.builder.ConfigurationContextBuilder;
import net.ecoporium.ecoporium.market.MarketWhitelistOptions;

import java.util.List;

/**
 * Builds whitelist options
 * {@code String} = the previous path
 * {@code ConfigurationAdapter} = the current config adapter context
 * {@code MarketWhitelistOptions} = the market whitelist options
 */
public class MarketWhitelistOptionsBuilder extends ConfigurationContextBuilder<String, MarketWhitelistOptions> {

    @Override
    public MarketWhitelistOptions build(String s, ConfigurationAdapter configurationAdapter) {
        // resolve whitelist 'list'
        List<String> tickers = configurationAdapter.getStringList(resolveConfigurationPath(s, "whitelist"), null);

        if (tickers == null) {
            return null;
        }

        return new MarketWhitelistOptions(tickers);
    }
}
