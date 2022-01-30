package com.yakovliam.ecoporium.market.stock.fake;

import java.util.concurrent.ThreadLocalRandom;

public class FakeStockProvider {

    /**
     * Previous closing price
     */
    private final Float previousClosingPrice;

    /**
     * Fake stock provider
     *
     * @param previousClosingPrice previous closing price
     */
    public FakeStockProvider(float previousClosingPrice) {
        this.previousClosingPrice = previousClosingPrice;
    }

    /**
     * Fake stock provider
     */
    public FakeStockProvider() {
        // generate ipo
        this.previousClosingPrice = generateRandomFloat(20f, 1000f);
    }

    /**
     * Generates a random float
     *
     * @param min min
     * @param max max
     * @return float
     */
    private float generateRandomFloat(float min, float max) {
        if (min >= max)
            throw new IllegalArgumentException("max must be greater than min");
        float result = ThreadLocalRandom.current().nextFloat() * (max - min) + min;
        if (result >= max) // correct for rounding
            result = Float.intBitsToFloat(Float.floatToIntBits(max) - 1);
        return result;
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
