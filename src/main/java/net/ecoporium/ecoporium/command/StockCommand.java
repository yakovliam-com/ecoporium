package net.ecoporium.ecoporium.command;

import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.CatchUnknown;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.HelpCommand;
import co.aikar.commands.annotation.Single;
import co.aikar.commands.annotation.Subcommand;
import net.ecoporium.ecoporium.EcoporiumPlugin;
import net.ecoporium.ecoporium.api.message.Message;
import net.ecoporium.ecoporium.market.FakeMarket;
import net.ecoporium.ecoporium.market.MarketType;
import net.ecoporium.ecoporium.market.RealMarket;
import net.ecoporium.ecoporium.user.EcoporiumUser;
import net.ecoporium.ecoporium.util.NumberUtil;
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

    @Subcommand("buy")
    @Description("Buys a stock from a particular market")
    public void onBuy(Player player, @Single String market, @Single String symbol, @Single Integer amountToBuy) {
        plugin.getMessages().retrievingMarket.message(player);

        // does market already exist?
        plugin.getMarketCache().getCache().get(market).thenAccept(marketObj -> {
            // if doesn't exist
            if (marketObj == null) {
                plugin.getMessages().marketNonexistent.message(player);
                return;
            }

            float pricePerShare;

            if (marketObj.getMarketType() == MarketType.FAKE) {
                FakeMarket fakeMarket = (FakeMarket) marketObj;

                // does the stock exist?
                if (!fakeMarket.getTickerCache().containsKey(symbol)) {
                    plugin.getMessages().marketSymbolDoesntExist.message(player);
                    return;
                }

                pricePerShare = fakeMarket.getTickerCache().get(symbol).getPrice();

            } else if (marketObj.getMarketType() == MarketType.REAL) {
                RealMarket realMarket = (RealMarket) marketObj;

                // does the stock exist?
                if (!realMarket.getTickerCache().containsKey(symbol)) {
                    plugin.getMessages().marketSymbolDoesntExist.message(player);
                    return;
                }

                pricePerShare = realMarket.getTickerCache().get(symbol).getCurrentStockData().getQuote().getPrice().floatValue();

            } else {
                plugin.getMessages().somethingWentWrong.message(player);
                return;
            }

            // if the user has enough to pay for the stocks they are buying
            float amountNeededToBuy = pricePerShare * amountToBuy;
            float balance = (float) plugin.getEconomy().getBalance(player);

            if (balance < amountNeededToBuy) {
                plugin.getMessages().stockBuyNotEnoughMoney.message(player, "%balance-needed%", NumberUtil.formatToPlaces(amountNeededToBuy, 2));
                return;
            }

            // get user
            EcoporiumUser user = plugin.getUserCache().getCache().get(player.getUniqueId()).join();

            // give user shares
            user.addShares(marketObj.getHandle(), symbol, amountToBuy);
            // save user
            plugin.getStorage().saveUser(user);

            // withdraw
            plugin.getEconomy().withdrawPlayer(player, amountNeededToBuy);

            plugin.getMessages().stockBuyBought.message(player,
                    "%symbol%", symbol,
                    "%shares%", Integer.toString(amountToBuy),
                    "%price-per-share%", NumberUtil.formatToPlaces(pricePerShare, 2),
                    "%amount-paid%", NumberUtil.formatToPlaces(amountNeededToBuy, 2));
        });
    }

    @Subcommand("sell")
    @Description("Sells a stock from a particular market")
    public void onSell(Player player, @Single String market, @Single String symbol, @Single Integer amountToSell) {
        plugin.getMessages().retrievingMarket.message(player);

        // does market already exist?
        plugin.getMarketCache().getCache().get(market).thenAccept(marketObj -> {
            // if doesn't exist
            if (marketObj == null) {
                plugin.getMessages().marketNonexistent.message(player);
                return;
            }

            float pricePerShare;

            if (marketObj.getMarketType() == MarketType.FAKE) {
                FakeMarket fakeMarket = (FakeMarket) marketObj;

                // does the stock exist?
                if (!fakeMarket.getTickerCache().containsKey(symbol)) {
                    plugin.getMessages().marketSymbolDoesntExist.message(player);
                    return;
                }

                pricePerShare = fakeMarket.getTickerCache().get(symbol).getPrice();

            } else if (marketObj.getMarketType() == MarketType.REAL) {
                RealMarket realMarket = (RealMarket) marketObj;

                // does the stock exist?
                if (!realMarket.getTickerCache().containsKey(symbol)) {
                    plugin.getMessages().marketSymbolDoesntExist.message(player);
                    return;
                }

                pricePerShare = realMarket.getTickerCache().get(symbol).getCurrentStockData().getQuote().getPrice().floatValue();

            } else {
                plugin.getMessages().somethingWentWrong.message(player);
                return;
            }

            // get user
            EcoporiumUser user = plugin.getUserCache().getCache().get(player.getUniqueId()).join();

            // if user doesn't have enough shares
            int sharesOwned = user.getShares(marketObj.getHandle(), symbol);

            if (sharesOwned < amountToSell) {
                plugin.getMessages().stockSellNotEnoughShares.message(player);
                return;
            }

            // calculate amount to give player
            float amountToGive = pricePerShare * amountToSell;
            // deposit
            plugin.getEconomy().depositPlayer(player, amountToGive);

            // remove user shares
            user.removeShares(marketObj.getHandle(), symbol, amountToSell);
            // save user
            plugin.getStorage().saveUser(user);

            plugin.getMessages().stockSellSold.message(player,
                    "%shares%", Integer.toString(amountToSell),
                    "%symbol%", symbol,
                    "%price-per-share%", NumberUtil.formatToPlaces(pricePerShare, 2),
                    "%amount-given%", NumberUtil.formatToPlaces(amountToGive, 2));
        });
    }

    @Subcommand("portfolio")
    @Description("Views your portfolio")
    public void onPorfolio(Player player) {
        // get user
        EcoporiumUser user = plugin.getUserCache().getCache().get(player.getUniqueId()).join();

        // portfolio header
        plugin.getMessages().stockPortfolioHeader.message(player);
        Message.Builder builder = Message.builder();

        user.getSharesOwnedTable().cellSet().forEach((cell) -> builder.addLine("&7- &f" + cell.getColumnKey() + "&8|&f" + cell.getRowKey() + "&7: &f" + cell.getValue()));

        builder.build().message(player);
    }

    @HelpCommand
    @Default
    @CatchUnknown
    public void doHelp(CommandSender sender, CommandHelp help) {
        plugin.getMessages().help.message(sender);
        help.showHelp();
    }

    @Override
    protected void registerCompletions() {
    }

    @Override
    protected void registerContexts() {
    }
}
