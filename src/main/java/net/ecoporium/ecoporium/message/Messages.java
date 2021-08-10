package net.ecoporium.ecoporium.message;

import net.ecoporium.ecoporium.api.message.Message;

public class Messages {

    /**
     * Ecoporium command
     */

    public final Message help = Message.builder()
            .addLine("&7Help")
            .build();

    // market nonexistent
    public final Message marketNonexistent = Message.builder()
            .addLine("&7That market does not exist.")
            .build();

    // market exists already
    public final Message marketExistsAlready = Message.builder()
            .addLine("&7A market with that name already exists.")
            .build();

    // something went wrong
    public final Message somethingWentWrong = Message.builder()
            .addLine("&cSomething went wrong!")
            .build();

    // retrieving market
    public final Message retrievingMarket = Message.builder()
            .addLine("&7Getting market data...")
            .build();

    // market deleted
    public final Message marketDeleted = Message.builder()
            .addLine("&7Market deleted.")
            .build();

    // market symbol already exists
    public final Message marketSymbolAlreadyExists = Message.builder()
            .addLine("&7That symbol already exists in that market.")
            .build();

    // market symbol doesn't exist
    public final Message marketSymbolDoesntExist = Message.builder()
            .addLine("&7That symbol doesn't exist in that market.")
            .build();

    // market created
    public final Message marketCreated = Message.builder()
            .addLine("&7Market created.")
            .build();

    // stock added
    public final Message stockAdded = Message.builder()
            .addLine("&7Stock added.")
            .build();

    // stock removed
    public final Message stockRemoved = Message.builder()
            .addLine("&7Stock removed.")
            .build();

    // screen create cancel not in placement session
    public Message screenCreateCancelNotInPlacementSession = Message.builder()
            .addLine("&cYou're not in a placement session!")
            .build();

    // screen create already in session
    public Message screenCreateAlreadyInSession = Message.builder()
            .addLine("&cYou're already in a placement session!")
            .build();

    // screen create canceled
    public Message screenCreateCanceled = Message.builder()
            .addLine("&7Canceled.")
            .build();

    // screen cant find
    public Message screenCantFind = Message.builder()
            .addLine("&7Can't find a screen that you're looking at")
            .build();

    // screen deleted
    public final Message screenDeleted = Message.builder()
            .addLine("&7Screen deleted.")
            .build();

    /**
     * Stock command
     */

    // stock portfolio header
    public final Message stockPortfolioHeader = Message.builder()
            .addLine("&7Portfolio:")
            .build();

    // stock buy not enough
    public final Message stockBuyNotEnoughMoney = Message.builder()
            .addLine("&7You need &f%balance-needed% &7to buy that much of that stock.")
            .build();

    // stock buy bought
    public final Message stockBuyBought = Message.builder()
            .addLine("&7You bought &f%shares% &7of &f%symbol% &7at &f%price-per-share% &7per share for a total of &f%amount-paid%&7.")
            .build();

    // stock sell not enough
    public final Message stockSellNotEnoughShares = Message.builder()
            .addLine("&7You don't own enough shares to sell that many.")
            .build();

    // stock sell sold
    public final Message stockSellSold = Message.builder()
            .addLine("&7You sold &f%shares% &7of &f%symbol% &7at &f%price-per-share% &7per share for a total of &f%amount-given%&7.")
            .build();

    /**
     * Placement
     */

    public final Message placementStartPlacing = Message.builder()
            .addLine("&7Start placing a &f%widthmaps%&7x&f%heightmaps% &7sized screen, going from &ftop to bottom &7& &fleft to right&7.")
            .build();

}
