package com.yakovliam.ecoporium.message;

import com.yakovliam.ecoporium.EcoporiumPlugin;
import com.yakovliam.ecoporium.api.config.generic.adapter.ConfigurationAdapter;
import com.yakovliam.ecoporium.api.message.Message;

public class Messages {

    // something went wrong
    public final Message somethingWentWrong;

    /**
     * Ecoporium command
     */

    // help
    public final Message ecoporiumHelp;

    // reloaded
    public final Message ecoporiumReloaded;

    // market nonexistent
    public final Message ecoporiumMarketNonexistent;

    // market exists already
    public final Message ecoporiumMarketExistsAlready;

    // retrieving market
    public final Message ecoporiumMarketGettingData;
    // market deleted
    public final Message ecoporiumMarketDeleted;

    // market created
    public final Message ecoporiumMarketCreated;

    // market symbol already exists
    public final Message ecoporiumMarketSymbolAlreadyExists;

    // market symbol doesn't exist
    public final Message ecoporiumMarketSymbolDoesntExist;

    // stock added
    public final Message ecoporiumMarketStockAdded;

    // stock removed
    public final Message ecoporiumMarketStockRemoved;
    // screen deleted
    public final Message ecoporiumScreenDeleteDeleted;
    /**
     * Stock command
     */

    // stock portfolio header
    public final Message stockPortfolio;
    // stock buy not enough
    public final Message stockBuyNotEnough;
    // stock buy bought
    public final Message stockBuyBought;
    // stock sell not enough
    public final Message stockSellNotEnough;
    // stock sell sold
    public final Message stockSellSold;
    // stock price
    public final Message stockPrice;
    // stock price not available
    public final Message stockPriceNotAvailable;
    // screen create cancel not in placement session
    public final Message ecoporiumScreenCreateNotInPlacementSession;
    // screen create already in session
    public final Message ecoporiumScreenCreateAlreadyInPlacementSession;
    // screen create canceled
    public final Message ecoporiumScreenCreateCanceled;
    // screen cant find
    public final Message ecoporiumScreenDeleteCantFind;

    /**
     * Messages
     *
     * @param plugin plugin
     */
    public Messages(EcoporiumPlugin plugin) {
        ConfigurationAdapter adapter = plugin.getLangConfig().getAdapter();

        somethingWentWrong = Message.fromConfigurationSection("something-went-wrong", adapter);
        ecoporiumHelp = Message.fromConfigurationSection("ecoporium.help", adapter);
        ecoporiumReloaded = Message.fromConfigurationSection("ecoporium.reloaded", adapter);
        ecoporiumMarketNonexistent = Message.fromConfigurationSection("ecoporium.market.nonexistent", adapter);
        ecoporiumMarketExistsAlready = Message.fromConfigurationSection("ecoporium.market.exists-already", adapter);
        ecoporiumMarketGettingData = Message.fromConfigurationSection("ecoporium.market.getting-data", adapter);
        ecoporiumMarketDeleted = Message.fromConfigurationSection("ecoporium.market.deleted", adapter);
        ecoporiumMarketCreated = Message.fromConfigurationSection("ecoporium.market.created", adapter);
        ecoporiumMarketSymbolAlreadyExists = Message.fromConfigurationSection("ecoporium.market.symbol-already-exists", adapter);
        ecoporiumMarketSymbolDoesntExist = Message.fromConfigurationSection("ecoporium.market.symbol-doesnt-exist", adapter);
        ecoporiumMarketStockAdded = Message.fromConfigurationSection("ecoporium.market.stock.added", adapter);
        ecoporiumMarketStockRemoved = Message.fromConfigurationSection("ecoporium.market.stock.removed", adapter);
        ecoporiumScreenCreateNotInPlacementSession = Message.fromConfigurationSection("ecoporium.screen.create.not-in-placement-session", adapter);
        ecoporiumScreenCreateAlreadyInPlacementSession = Message.fromConfigurationSection("ecoporium.screen.create.already-in-placement-session", adapter);
        ecoporiumScreenCreateCanceled = Message.fromConfigurationSection("ecoporium.screen.create.canceled", adapter);
        ecoporiumScreenDeleteCantFind = Message.fromConfigurationSection("ecoporium.screen.delete.cant-find", adapter);
        ecoporiumScreenDeleteDeleted = Message.fromConfigurationSection("ecoporium.screen.delete.deleted", adapter);
        stockPortfolio = Message.fromConfigurationSection("stock.portfolio", adapter);
        stockBuyNotEnough = Message.fromConfigurationSection("stock.buy.not-enough", adapter);
        stockBuyBought = Message.fromConfigurationSection("stock.buy.bought", adapter);
        stockSellNotEnough = Message.fromConfigurationSection("stock.sell.not-enough", adapter);
        stockSellSold = Message.fromConfigurationSection("stock.sell.sold", adapter);
        stockPrice = Message.fromConfigurationSection("stock.price", adapter);
        stockPriceNotAvailable = Message.fromConfigurationSection("stock.price-not-available", adapter);
    }
}
