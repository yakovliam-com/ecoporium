package com.yakovliam.ecoporium.api.user;

import com.google.common.collect.Table;

import java.util.UUID;

public abstract class EcoporiumUser {

    /**
     * Returns the uuid
     *
     * @return uuid
     */
    public abstract UUID getUuid();

    /**
     * Returns the user's shares table
     *
     * @return shares table
     */
    public abstract Table<String, String, Integer> getSharesOwnedTable();

    /**
     * Add shares
     *
     * @param marketHandle market handle
     * @param symbol       symbol
     * @param amountToAdd  amount to add
     */
    public abstract void addShares(String marketHandle, String symbol, Integer amountToAdd);

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
    public abstract int getShares(String marketHandle, String symbol);
}
