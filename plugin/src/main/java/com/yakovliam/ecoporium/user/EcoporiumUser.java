package com.yakovliam.ecoporium.user;

import com.google.common.collect.Table;

import java.util.UUID;

public class EcoporiumUser {

    /**
     * The uuid of the associated player
     */
    private final UUID uuid;

    /**
     * User shares map
     * <p>
     * [market handle, symbol, shares owned]
     */
    private final Table<String, String, Integer> sharesOwnedTable;

    /**
     * Ecoporium user
     *
     * @param uuid             uuid
     * @param sharesOwnedTable shares table
     */
    public EcoporiumUser(UUID uuid, Table<String, String, Integer> sharesOwnedTable) {
        this.uuid = uuid;
        this.sharesOwnedTable = sharesOwnedTable;
    }

    /**
     * Returns the uuid
     *
     * @return uuid
     */
    public UUID getUuid() {
        return uuid;
    }

    /**
     * Returns the user's shares table
     *
     * @return shares table
     */
    public Table<String, String, Integer> getSharesOwnedTable() {
        return sharesOwnedTable;
    }

    /**
     * Add shares
     *
     * @param marketHandle market handle
     * @param symbol       symbol
     * @param amountToAdd  amount to add
     */
    public void addShares(String marketHandle, String symbol, Integer amountToAdd) {
        int currentShares = 0;
        if (sharesOwnedTable.contains(marketHandle, symbol)) {
            currentShares = sharesOwnedTable.get(marketHandle, symbol);
        }

        sharesOwnedTable.put(marketHandle, symbol, currentShares + amountToAdd);
    }

    /**
     * Removes shares
     *
     * @param marketHandle   market handle
     * @param symbol         symbol
     * @param amountToRemove amount to remove
     */
    public void removeShares(String marketHandle, String symbol, Integer amountToRemove) {
        int currentShares = 0;
        if (sharesOwnedTable.contains(marketHandle, symbol)) {
            currentShares = sharesOwnedTable.get(marketHandle, symbol);
        }

        if ((currentShares - amountToRemove) <= 0) {
            sharesOwnedTable.remove(marketHandle, symbol);
        } else {
            sharesOwnedTable.put(marketHandle, symbol, currentShares - amountToRemove);
        }
    }

    /**
     * Get shares for a symbol
     *
     * @param symbol       symbol
     * @param marketHandle market handle
     * @return shares
     */
    public int getShares(String marketHandle, String symbol) {
        return sharesOwnedTable.contains(marketHandle, symbol) ? sharesOwnedTable.get(marketHandle, symbol) : 0;
    }
}
