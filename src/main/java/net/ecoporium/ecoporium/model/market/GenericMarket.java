package net.ecoporium.ecoporium.model.market;

public class GenericMarket extends Market {

    /**
     * Market
     *
     * @param handle           handle
     * @param whitelistOptions whitelist options
     */
    public GenericMarket(String handle, MarketWhitelistOptions whitelistOptions) {
        super(handle, whitelistOptions);
    }
}
