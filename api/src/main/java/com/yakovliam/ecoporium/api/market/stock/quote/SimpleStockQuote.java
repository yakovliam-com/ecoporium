package com.yakovliam.ecoporium.api.market.stock.quote;

import java.util.Date;

public class SimpleStockQuote {

    /**
     * The price at the time that this quote was created
     */
    private final float price;

    /**
     * The date when this quote was created
     */
    private final Date date;

    /**
     * Simple stock quote
     *
     * @param price price
     * @param date  date
     */
    public SimpleStockQuote(float price, Date date) {
        this.price = price;
        this.date = date;
    }

    /**
     * Returns the price
     *
     * @return price
     */
    public float getPrice() {
        return price;
    }

    /**
     * Returns the date
     *
     * @return date
     */
    public Date getDate() {
        return date;
    }
}
