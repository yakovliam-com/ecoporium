package com.yakovliam.ecoporium.api.market.stock.fake;

public interface FakeStockProvider {

    /**
     * Calculates the opening price
     *
     * @return opening price
     */
    float calculateOpeningPrice();

    /**
     * Calculates the next price from previous
     *
     * @param previous previous
     * @return next price
     */
    float calculatePrice(float previous);
}
