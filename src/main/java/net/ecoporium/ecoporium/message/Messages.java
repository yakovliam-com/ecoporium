package net.ecoporium.ecoporium.message;

import net.ecoporium.ecoporium.api.message.Message;

public class Messages {

    /**
     * Ecoporium command
     */

    public Message help = Message.builder()
            .addLine("&7Help")
            .build();

    // market nonexistent
    public Message marketNonexistent = Message.builder()
            .addLine("&7That market does not exist.")
            .build();

    // market exists already
    public Message marketExistsAlready = Message.builder()
            .addLine("&7A market with that name already exists.")
            .build();

    // something went wrong
    public Message somethingWentWrong = Message.builder()
            .addLine("&cSomething went wrong!")
            .build();

    // retrieving market
    public Message retrievingMarket = Message.builder()
            .addLine("&7Getting market data...")
            .build();

    // market deleted
    public Message marketDeleted = Message.builder()
            .addLine("&7Market deleted.")
            .build();

    // market symbol already exists
    public Message marketSymbolAlreadyExists = Message.builder()
            .addLine("&7That symbol already exists in that market.")
            .build();

    // market symbol doesn't exist
    public Message marketSymbolDoesntExist = Message.builder()
            .addLine("&7That symbol doesn't exist in that market.")
            .build();

    // market created
    public Message marketCreated = Message.builder()
            .addLine("&7Market created.")
            .build();

    // stock added
    public Message stockAdded = Message.builder()
            .addLine("&7Stock added.")
            .build();

    // stock removed
    public Message stockRemoved = Message.builder()
            .addLine("&7Stock removed.")
            .build();

    /**
     * Stock command
     */

    // stock portfolio header
    public Message stockPortfolioHeader = Message.builder()
            .addLine("&7Portfolio:")
            .build();

    // stock buy not enough
    public Message stockBuyNotEnoughMoney = Message.builder()
            .addLine("&7You need &f%balance-needed% &7to buy that much of that stock.")
            .build();

    // stock buy bought
    public Message stockBuyBought = Message.builder()
            .addLine("&7You bought &f%shares% &7of &f%symbol% &7at &f%price-per-share% &7per share for a total of &f%amount-paid%&7.")
            .build();

    // stock sell not enough
    public Message stockSellNotEnoughShares = Message.builder()
            .addLine("&7You don't own enough shares to sell that many.")
            .build();

    // stock sell sold
    public Message stockSellSold = Message.builder()
            .addLine("&7You sold &f%shares% &7of &f%symbol% &7at &f%price-per-share% &7per share for a total of &f%amount-given%&7.")
            .build();
}
