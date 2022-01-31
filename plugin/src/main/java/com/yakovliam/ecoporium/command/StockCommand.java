package com.yakovliam.ecoporium.command;

import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import com.yakovliam.ecoporium.EcoporiumPlugin;
import com.yakovliam.ecoporium.api.message.Message;
import com.yakovliam.ecoporium.api.market.stock.StockTicker;
import com.yakovliam.ecoporium.api.user.EcoporiumUser;
import com.yakovliam.ecoporium.user.EcoporiumUserImpl;
import com.yakovliam.ecoporium.util.NumberUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("stock")
@CommandPermission("ecoporium.command.stock")
public class StockCommand extends AbstractEcoporiumCommand {

    /**
     * Ecoporium command
     *
     * @param manager manager
     * @param plugin  plugin
     */
    public StockCommand(CommandManager manager, EcoporiumPlugin plugin) {
        super(manager, plugin);
    }

    @Subcommand("buy|b")
    @Description("Buys a stock from a particular market")
    public void onBuy(Player player, @Single String market, @Single String symbol, @Single Integer amountToBuy) {
        plugin.getMessages().ecoporiumMarketGettingData.message(player);

        // does market already exist?
        plugin.getMarketCache().getCache().get(market).thenAccept(marketObj -> {
            // if doesn't exist
            if (marketObj == null) {
                plugin.getMessages().ecoporiumMarketNonexistent.message(player);
                return;
            }

            float pricePerShare;

            // does the stock exist?
            if (!marketObj.containsStock(symbol)) {
                plugin.getMessages().ecoporiumMarketSymbolDoesntExist.message(player);
                return;
            }

            StockTicker<?> stockTicker = marketObj.getStock(symbol);
            pricePerShare = stockTicker.getCurrentQuote().getPrice();

            // if the user has enough to pay for the stocks they are buying
            float amountNeededToBuy = pricePerShare * amountToBuy;
            float balance = (float) plugin.getEconomy().getBalance(player);

            if (balance < amountNeededToBuy) {
                plugin.getMessages().stockBuyNotEnough.message(player, "%balance-needed%", NumberUtil.formatToPlaces(amountNeededToBuy, 2));
                return;
            }

            // get user
            EcoporiumUserImpl user = plugin.getUserCache().getCache().get(player.getUniqueId()).join();

            // give user shares
            user.addShares(marketObj.getHandle(), stockTicker.getSymbol(), amountToBuy);
            // save user
            plugin.getStorage().saveUser(user, true);

            // withdraw
            plugin.getEconomy().withdrawPlayer(player, amountNeededToBuy);

            plugin.getMessages().stockBuyBought.message(player,
                    "%symbol%", symbol,
                    "%shares%", Integer.toString(amountToBuy),
                    "%price-per-share%", NumberUtil.formatToPlaces(pricePerShare, 2),
                    "%amount-paid%", NumberUtil.formatToPlaces(amountNeededToBuy, 2));
        });
    }

    @Subcommand("sell|s")
    @Description("Sells a stock from a particular market")
    public void onSell(Player player, @Single String market, @Single String symbol, @Single Integer amountToSell) {
        plugin.getMessages().ecoporiumMarketGettingData.message(player);

        // does market already exist?
        plugin.getMarketCache().getCache().get(market).thenAccept(marketObj -> {
            // if doesn't exist
            if (marketObj == null) {
                plugin.getMessages().ecoporiumMarketNonexistent.message(player);
                return;
            }

            float pricePerShare;

            // does the stock exist?
            if (!marketObj.containsStock(symbol)) {
                plugin.getMessages().ecoporiumMarketSymbolDoesntExist.message(player);
                return;
            }

            StockTicker<?> stockTicker = marketObj.getStock(symbol);
            pricePerShare = stockTicker.getCurrentQuote().getPrice();

            // get user
            EcoporiumUserImpl user = plugin.getUserCache().getCache().get(player.getUniqueId()).join();

            // if user doesn't have enough shares
            int sharesOwned = user.getShares(marketObj.getHandle(), stockTicker.getSymbol());

            if (sharesOwned < amountToSell) {
                plugin.getMessages().stockSellNotEnough.message(player);
                return;
            }

            // calculate amount to give player
            float amountToGive = pricePerShare * amountToSell;
            // deposit
            plugin.getEconomy().depositPlayer(player, amountToGive);

            // remove user shares
            user.removeShares(marketObj.getHandle(), stockTicker.getSymbol(), amountToSell);
            // save user
            plugin.getStorage().saveUser(user, true);

            plugin.getMessages().stockSellSold.message(player,
                    "%shares%", Integer.toString(amountToSell),
                    "%symbol%", stockTicker.getSymbol(),
                    "%price-per-share%", NumberUtil.formatToPlaces(pricePerShare, 2),
                    "%amount-given%", NumberUtil.formatToPlaces(amountToGive, 2));
        });
    }

    @Subcommand("price|pr")
    @Description("Views a stock's price")
    public void onPrice(Player player, @Single String market, @Single String symbol) {
        plugin.getMessages().ecoporiumMarketGettingData.message(player);

        // does market already exist?
        plugin.getMarketCache().getCache().get(market).thenAccept(marketObj -> {
            // if doesn't exist
            if (marketObj == null) {
                plugin.getMessages().ecoporiumMarketNonexistent.message(player);
                return;
            }

            float pricePerShare;

            // does the stock exist?
            if (!marketObj.containsStock(symbol)) {
                plugin.getMessages().ecoporiumMarketSymbolDoesntExist.message(player);
                return;
            }

            StockTicker<?> stockTicker = marketObj.getStock(symbol);
            pricePerShare = stockTicker.getCurrentQuote().getPrice();

            plugin.getMessages().stockPrice.message(player,
                    "%symbol%", stockTicker.getSymbol(),
                    "%price-per-share%", NumberUtil.formatToPlaces(pricePerShare, 2)
            );
        });
    }

    @Subcommand("portfolio|po")
    @Description("Views your portfolio")
    public void onPorfolio(Player player) {
        // get user
        EcoporiumUser user = plugin.getUserCache().getCache().get(player.getUniqueId()).join();

        // portfolio header
        plugin.getMessages().stockPortfolio.message(player);
        Message.Builder builder = Message.builder()
                .addLine("&7&m--------");

        user.getSharesOwnedTable().cellSet().forEach((cell) -> builder.addLine("&7- &f" + cell.getRowKey() + "&8|&f" + cell.getColumnKey() + "&7: &f" + cell.getValue()));

        builder.build().message(player);
    }

    @HelpCommand
    @Default
    @CatchUnknown
    public void doHelp(CommandSender sender, CommandHelp help) {
        plugin.getMessages().ecoporiumHelp.message(sender);
        help.showHelp();
    }

    @Override
    protected void registerCompletions() {
    }

    @Override
    protected void registerContexts() {
    }
}
