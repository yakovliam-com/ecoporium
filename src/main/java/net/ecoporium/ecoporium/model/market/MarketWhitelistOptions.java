package net.ecoporium.ecoporium.model.market;

import java.util.List;

public class MarketWhitelistOptions {

    /**
     * A list of tickers that will be whitelisted to the associated market
     */
    private final List<String> tickers;

    /**
     * Market whitelist options
     *
     * @param tickers tickers
     */
    public MarketWhitelistOptions(List<String> tickers) {
        this.tickers = tickers;
    }

    /**
     * Returns the tickers
     *
     * @return tickers
     */
    public List<String> getTickers() {
        return tickers;
    }
}
