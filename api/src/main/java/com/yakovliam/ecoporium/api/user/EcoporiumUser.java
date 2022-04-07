package com.yakovliam.ecoporium.api.user;

import com.google.common.collect.Table;
import com.yakovliam.ecoporium.api.user.share.OwnedShare;

import java.util.List;
import java.util.UUID;

public abstract class EcoporiumUser {

    /**
     * Returns the uuid
     *
     * @return uuid
     */
    public abstract UUID uuid();

    /**
     * Returns the user's shares table
     *
     * @return shares table
     */
    public abstract Table<String, String, List<OwnedShare>> sharesOwnedTable();

    /**
     * Add shares
     *
     * @param marketHandle     market handle
     * @param symbol           symbol
     * @param amountOfShares   amount of shares
     * @param priceOfEachShare the price of each share
     */
    public abstract void addShares(String marketHandle, String symbol, Integer amountOfShares, Float priceOfEachShare);

    /**
     * Removes shares
     *
     * @param marketHandle   market handle
     * @param symbol         symbol
     * @param amountToRemove amount to remove
     */
    public abstract void removeShares(String marketHandle, String symbol, Integer amountToRemove);

    /**
     * Get shares for a symbol
     *
     * @param symbol       symbol
     * @param marketHandle market handle
     * @return shares
     */
    public abstract int numberOfShares(String marketHandle, String symbol);

    /**
     * Get total number of shares
     *
     * @return shares
     */
    public abstract int totalNumberOfShares();

    /**
     * Get owned shares object array for a symbol
     *
     * @param symbol       symbol
     * @param marketHandle market handle
     * @return shares
     */
    public abstract List<OwnedShare> ownedShares(String marketHandle, String symbol);
}
