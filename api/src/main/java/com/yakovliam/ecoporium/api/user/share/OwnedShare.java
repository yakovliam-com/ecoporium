package com.yakovliam.ecoporium.api.user.share;

public class OwnedShare {

    /**
     * The price of this share
     */
    private final float priceOfShare;

    /**
     * Owned share
     *
     * @param priceOfShare price of this share
     */
    public OwnedShare(float priceOfShare) {
        this.priceOfShare = priceOfShare;
    }

    /**
     * Returns the price of this share
     *
     * @return price of this share
     */
    public float priceOfEachShare() {
        return priceOfShare;
    }
}
