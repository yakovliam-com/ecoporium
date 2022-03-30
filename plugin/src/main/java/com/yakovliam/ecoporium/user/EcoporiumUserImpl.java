package com.yakovliam.ecoporium.user;

import com.google.common.collect.Table;
import com.yakovliam.ecoporium.api.user.EcoporiumUser;
import com.yakovliam.ecoporium.api.user.share.OwnedShare;

import java.util.*;
import java.util.stream.Stream;

public class EcoporiumUserImpl extends EcoporiumUser {

    /**
     * The uuid of the associated player
     */
    private final UUID uuid;

    /**
     * User shares map
     * <p>
     * [market handle, symbol, shares owned]
     */
    private final Table<String, String, List<OwnedShare>> sharesOwnedTable;

    /**
     * Ecoporium user
     *
     * @param uuid             uuid
     * @param sharesOwnedTable shares table
     */
    public EcoporiumUserImpl(UUID uuid, Table<String, String, List<OwnedShare>> sharesOwnedTable) {
        this.uuid = uuid;
        this.sharesOwnedTable = sharesOwnedTable;
    }

    /**
     * Returns the uuid
     *
     * @return uuid
     */
    @Override
    public UUID getUuid() {
        return uuid;
    }

    /**
     * Returns the user's shares table
     *
     * @return shares table
     */
    @Override
    public Table<String, String, List<OwnedShare>> getSharesOwnedTable() {
        return sharesOwnedTable;
    }

    /**
     * Add shares
     *
     * @param marketHandle     market handle
     * @param symbol           symbol
     * @param amountOfShares   amount of shares
     * @param priceOfEachShare the price of each share
     */
    @Override
    public void addShares(String marketHandle, String symbol, Integer amountOfShares, Float priceOfEachShare) {
        List<OwnedShare> currentShares = new ArrayList<>();
        if (sharesOwnedTable.contains(marketHandle, symbol)) {
            List<OwnedShare> value = sharesOwnedTable.get(marketHandle, symbol);
            if (value != null) {
                currentShares = value;
            }
        }

        // add new shares
        for (int i = 0; i < amountOfShares; i++) {
            currentShares.add(new OwnedShare(priceOfEachShare));
        }

        sharesOwnedTable.put(marketHandle, symbol, currentShares);
    }

    /**
     * Removes shares
     *
     * @param marketHandle   market handle
     * @param symbol         symbol
     * @param amountToRemove amount to remove
     */
    @Override
    public void removeShares(String marketHandle, String symbol, Integer amountToRemove) {
        List<OwnedShare> currentShares = new ArrayList<>();
        if (sharesOwnedTable.contains(marketHandle, symbol)) {
            List<OwnedShare> value = sharesOwnedTable.get(marketHandle, symbol);
            if (value != null) {
                currentShares = value;
            }
        }

        int amountOfSharesOwned = currentShares.size();

        if (amountOfSharesOwned - amountToRemove <= 0) {
            sharesOwnedTable.remove(marketHandle, symbol);
            return;
        }

        // remove x # of stocks
        // currently stocks are removed essentially at random
        // todo add a way to chose which stocks are removed? or calculate which would just be best for the customer
        currentShares.subList(0, amountToRemove).clear();

        sharesOwnedTable.put(marketHandle, symbol, currentShares);
    }

    /**
     * Get shares for a symbol
     *
     * @param symbol       symbol
     * @param marketHandle market handle
     * @return shares
     */
    @Override
    public int getNumberOfShares(String marketHandle, String symbol) {
        return getOwnedShares(marketHandle, symbol).size();
    }

    @Override
    public int getTotalNumberOfShares() {
        return sharesOwnedTable.columnMap()
                .values()
                .stream()
                .flatMap(map -> Stream.of(map.values()))
                .flatMap(Collection::stream)
                .mapToInt(Collection::size)
                .sum();

    }

    /**
     * Get owned shares object array for a symbol
     *
     * @param symbol       symbol
     * @param marketHandle market handle
     * @return shares
     */
    @Override
    public List<OwnedShare> getOwnedShares(String marketHandle, String symbol) {
        return sharesOwnedTable.contains(marketHandle, symbol) ? Objects.requireNonNull(sharesOwnedTable.get(marketHandle, symbol)) : new ArrayList<>();
    }
}
