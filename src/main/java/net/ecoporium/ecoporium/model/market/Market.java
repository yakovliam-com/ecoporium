package net.ecoporium.ecoporium.model.market;

public abstract class Market {

    /**
     * The given name of the market
     * <p>
     * This is configurable and may look something like
     * "Stock Market" or "Crypto"
     * <p>
     * The associated value is up to the server owner
     */
    private String handle;

    /**
     * The market's whitelisted ticker options
     * <p>
     * If this is {@code null}, then all stock ticker symbols are allowed
     * If it is NOT {@code null}, then only the given ticker symbols will be allowed to be traded
     */
    private MarketWhitelistOptions whitelistOptions;

    /**
     * Returns the handle
     *
     * @return handle
     */
    public String getHandle() {
        return handle;
    }

    /**
     * Sets the handle
     *
     * @param handle handle
     * @return this
     */
    public Market setHandle(String handle) {
        this.handle = handle;
        return this;
    }

    /**
     * Returns the whitelist options
     *
     * @return whitelist options
     */
    public MarketWhitelistOptions getWhitelistOptions() {
        return whitelistOptions;
    }

    /**
     * Sets the whitelist options
     *
     * @param whitelistOptions whitelist options
     * @return this
     */
    public Market setWhitelistOptions(MarketWhitelistOptions whitelistOptions) {
        this.whitelistOptions = whitelistOptions;
        return this;
    }
}
