package net.ecoporium.ecoporium.placement;

import net.ecoporium.ecoporium.model.market.Market;
import net.ecoporium.ecoporium.ticker.info.ScreenInfo;

public class PlacementData {

    /**
     * Screen info
     */
    private final ScreenInfo screenInfo;

    /**
     * Market
     */
    private final Market market;

    /**
     * Symbol
     */
    private final String symbol;

    /**
     * Placement data
     *
     * @param screenInfo screen info
     * @param market     market
     * @param symbol     symbol
     */
    public PlacementData(ScreenInfo screenInfo, Market market, String symbol) {
        this.screenInfo = screenInfo;
        this.market = market;
        this.symbol = symbol;
    }

    /**
     * Returns screen info
     *
     * @return screen info
     */
    public ScreenInfo getScreenInfo() {
        return screenInfo;
    }

    /**
     * Returns market
     *
     * @return market
     */
    public Market getMarket() {
        return market;
    }

    /**
     * Returns symbol
     *
     * @return symbol
     */
    public String getSymbol() {
        return symbol;
    }
}
