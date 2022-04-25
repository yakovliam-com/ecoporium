package com.yakovliam.ecoporium.config.messages;

import com.yakovliam.ecoporium.api.config.ConfigHolder;
import net.kyori.adventure.text.Component;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.*;
import static net.kyori.adventure.text.format.TextDecoration.ITALIC;

@ConfigSerializable
@SuppressWarnings("FieldMayBeFinal")
public class MessagesHolder implements ConfigHolder {

    private Component somethingWentWrong = text("Something went wrong!", RED);

    private Component ecoporiumHelp = text("Help", GRAY);
    private Component ecoporiumReloaded = text("Reloaded.", GRAY);
    private Component ecoporiumMarketNonexistent = text("That market does not exist.", RED);
    private Component ecoporiumMarketExistsAlready = text("A market with that name already exists.", RED);
    private Component ecoporiumMarketGettingData = text("Getting market data...", GRAY);
    private Component ecoporiumMarketDeleted = text("Deleted.", GRAY);
    private Component ecoporiumMarketCreated = text("Created.", GRAY);
    private Component ecoporiumMarketSymbolAlreadyExists = text("That symbol already exists in that market.", GRAY);
    private Component ecoporiumMarketSymbolDoesntExist = text("That symbol doesn't exist in that market.", RED);
    private Component ecoporiumMarketStockAdded = text("Added.", GRAY);
    private Component ecoporiumMarketStockDeleted = text("Deleted.", GRAY);

    private Component stockPortfolio = text("Portfolio", GRAY);
    private Component stockPortfolioItem = text("- ", GRAY)
            .append(text("%market%", WHITE))
            .append(text("|", DARK_GRAY))
            .append(text("%stock%", WHITE))
            .append(text(": ", GRAY))
            .append(text("%shares-amount%%position%", WHITE));
    private Component stockPortfolioPositionUp = text("(⬆ %percent%%) ", GREEN, ITALIC);
    private Component stockPortfolioPositionDown = text("(⬇ %percent%%) ", RED, ITALIC);
    private Component stockPortfolioPositionUnchanged = text("(~ %percent%%) ", GRAY, ITALIC);
    private Component stockBuyNotEnough = text("You need ", GRAY)
            .append(text("%balance-needed% ", WHITE))
            .append(text("to buy that much of that stock.", GRAY));
    private Component stockBuyBought = text("You bought ", GRAY)
            .append(text("%shares% ", WHITE))
            .append(text("of ", GRAY))
            .append(text("%symbol% ", WHITE))
            .append(text("at ", GRAY))
            .append(text("%price-per-share% ", WHITE))
            .append(text("per share for a total of ", GRAY))
            .append(text("%amount-paid%", WHITE))
            .append(text(".", GRAY));
    private Component stockSellNotEnough = text("You don't own enough shares to sell that many.", RED);
    private Component stockSellSold = text("You sold ", GRAY)
            .append(text("%shares% ", WHITE))
            .append(text("of ", GRAY))
            .append(text("%symbol% ", WHITE))
            .append(text("at ", GRAY))
            .append(text("%price-per-share% ", WHITE))
            .append(text("per share for a total of ", GRAY))
            .append(text("%amount-given%", WHITE))
            .append(text(".", GRAY));
    private Component stockPrice = text("Current price of ", GRAY)
            .append(text("%symbol%", WHITE))
            .append(text(": ", GRAY))
            .append(text("%price-per-share%", GOLD));
    private Component stockPriceNotAvailable = text("The price is not yet available; we're working on it!", GRAY);

    public Component somethingWentWrong() {
        return somethingWentWrong;
    }

    public Component ecoporiumHelp() {
        return ecoporiumHelp;
    }

    public Component ecoporiumReloaded() {
        return ecoporiumReloaded;
    }

    public Component ecoporiumMarketNonexistent() {
        return ecoporiumMarketNonexistent;
    }

    public Component ecoporiumMarketExistsAlready() {
        return ecoporiumMarketExistsAlready;
    }

    public Component ecoporiumMarketGettingData() {
        return ecoporiumMarketGettingData;
    }

    public Component ecoporiumMarketDeleted() {
        return ecoporiumMarketDeleted;
    }

    public Component ecoporiumMarketCreated() {
        return ecoporiumMarketCreated;
    }

    public Component ecoporiumMarketSymbolAlreadyExists() {
        return ecoporiumMarketSymbolAlreadyExists;
    }

    public Component ecoporiumMarketSymbolDoesntExist() {
        return ecoporiumMarketSymbolDoesntExist;
    }

    public Component ecoporiumMarketStockAdded() {
        return ecoporiumMarketStockAdded;
    }

    public Component ecoporiumMarketStockDeleted() {
        return ecoporiumMarketStockDeleted;
    }

    public Component stockPortfolio() {
        return stockPortfolio;
    }

    public Component stockPortfolioItem() {
        return stockPortfolioItem;
    }

    public Component stockPortfolioPositionUp() {
        return stockPortfolioPositionUp;
    }

    public Component stockPortfolioPositionDown() {
        return stockPortfolioPositionDown;
    }

    public Component stockPortfolioPositionUnchanged() {
        return stockPortfolioPositionUnchanged;
    }

    public Component stockBuyNotEnough() {
        return stockBuyNotEnough;
    }

    public Component stockBuyBought() {
        return stockBuyBought;
    }

    public Component stockSellNotEnough() {
        return stockSellNotEnough;
    }

    public Component stockSellSold() {
        return stockSellSold;
    }

    public Component stockPrice() {
        return stockPrice;
    }

    public Component stockPriceNotAvailable() {
        return stockPriceNotAvailable;
    }
}
