package net.ecoporium.ecoporium.market.stock;

import java.util.concurrent.ThreadLocalRandom;

public class FakeStockProvider {

    /**
     * Previous closing price
     */
    private final float previousClosingPrice;

    /**
     * Fake stock provider
     *
     * @param previousClosingPrice previous closing price
     */
    public FakeStockProvider(float previousClosingPrice) {
        this.previousClosingPrice = previousClosingPrice;
    }

    /**
     * Calculates the opening price
     *
     * @return opening price
     */
    public float calculateOpeningPrice() {
        return calculatePrice(previousClosingPrice);
    }

    /**
     * Calculates the next price from previous
     *
     * @param previous previous
     * @return next price
     */
    public float calculatePrice(float previous) {
        // Instead of a fixed volatility, pick a random volatility
        // each time, between 2 and 10.
        float volatility = ThreadLocalRandom.current().nextFloat() * 10 + 2;

        float rnd = ThreadLocalRandom.current().nextFloat();

        float changePercent = 2 * volatility * rnd;

        if (changePercent > volatility) {
            changePercent -= (2 * volatility);
        }
        float changeAmount = previous * changePercent / 100;
        float newPrice = previous + changeAmount;

        // Add a ceiling and floor.
        float MAX_PRICE = 12_000f;
        float MIN_PRICE = 1.f;
        if (newPrice < MIN_PRICE) {
            newPrice += Math.abs(changeAmount) * 2;
        } else if (newPrice > MAX_PRICE) {
            newPrice -= Math.abs(changeAmount) * 2;
        }

        return newPrice;
    }
}
