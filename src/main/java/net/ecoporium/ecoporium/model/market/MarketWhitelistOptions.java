package net.ecoporium.ecoporium.model.market;

import java.util.List;

public class MarketWhitelistOptions {

    /**
     * A list of tickers that will be whitelisted to the associated market
     */
    private List<String> tickers;

    /**
     * Returns the tickers
     *
     * @return tickers
     */
    public List<String> getTickers() {
        return tickers;
    }

    /**
     * Sets the tickers
     *
     * @param tickers tickers
     * @return this
     */
    public MarketWhitelistOptions setTickers(List<String> tickers) {
        this.tickers = tickers;
        return this;
    }
}
