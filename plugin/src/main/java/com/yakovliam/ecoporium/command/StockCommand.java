package com.yakovliam.ecoporium.command;

import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import com.yakovliam.ecoporium.EcoporiumPlugin;
import com.yakovliam.ecoporium.api.market.Market;
import com.yakovliam.ecoporium.api.market.stock.StockTicker;
import com.yakovliam.ecoporium.api.market.stock.quote.SimpleStockQuote;
import com.yakovliam.ecoporium.api.message.Message;
import com.yakovliam.ecoporium.api.user.EcoporiumUser;
import com.yakovliam.ecoporium.api.user.share.OwnedShare;
import com.yakovliam.ecoporium.user.EcoporiumUserImpl;
import com.yakovliam.ecoporium.util.NumberUtil;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.concurrent.CompletableFuture;

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

    @Override
    protected void registerContexts() {
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
            if (stockTicker.getCurrentQuote().isEmpty()) {
                plugin.getMessages().stockPriceNotAvailable.message(player);
                return;
            }

            pricePerShare = stockTicker.getCurrentQuote().get().getPrice();

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
            user.addShares(marketObj.getHandle(), stockTicker.getSymbol(), amountToBuy, pricePerShare);
            // save user
            plugin.getStorage().saveUser(user, true);

            // withdraw
            plugin.getEconomy().withdrawPlayer(player, amountNeededToBuy);

            plugin.getMessages().stockBuyBought.message(player, "%symbol%", symbol, "%shares%", Integer.toString(amountToBuy), "%price-per-share%", NumberUtil.formatToPlaces(pricePerShare, 2), "%amount-paid%", NumberUtil.formatToPlaces(amountNeededToBuy, 2));
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

            if (stockTicker.getCurrentQuote().isEmpty()) {
                plugin.getMessages().stockPriceNotAvailable.message(player);
                return;
            }

            pricePerShare = stockTicker.getCurrentQuote().get().getPrice();

            // get user
            EcoporiumUserImpl user = plugin.getUserCache().getCache().get(player.getUniqueId()).join();

            // if user doesn't have enough shares
            int sharesOwned = user.getNumberOfShares(marketObj.getHandle(), stockTicker.getSymbol());

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

            plugin.getMessages().stockSellSold.message(player, "%shares%", Integer.toString(amountToSell), "%symbol%", stockTicker.getSymbol(), "%price-per-share%", NumberUtil.formatToPlaces(pricePerShare, 2), "%amount-given%", NumberUtil.formatToPlaces(amountToGive, 2));
        });
    }

    @Subcommand("price|pr")
    @Description("Views a stock's price")
    public void onPrice(CommandSender sender, @Single String market, @Single String symbol) {
        plugin.getMessages().ecoporiumMarketGettingData.message(sender);

        // does market already exist?
        plugin.getMarketCache().getCache().get(market).thenAccept(marketObj -> {
            // if doesn't exist
            if (marketObj == null) {
                plugin.getMessages().ecoporiumMarketNonexistent.message(sender);
                return;
            }

            float pricePerShare;

            // does the stock exist?
            if (!marketObj.containsStock(symbol)) {
                plugin.getMessages().ecoporiumMarketSymbolDoesntExist.message(sender);
                return;
            }

            StockTicker<?> stockTicker = marketObj.getStock(symbol);
            stockTicker.getCurrentQuote().ifPresentOrElse((quote) -> plugin.getMessages().stockPrice.message(sender, "%symbol%", stockTicker.getSymbol(), "%price-per-share%", NumberUtil.formatToPlaces(quote.getPrice(), 2)), () -> plugin.getMessages().stockPriceNotAvailable.message(sender));
        });
    }

    @Subcommand("portfolio|po")
    @Description("Views your portfolio")
    public void onPortfolio(Player player) {
        // get user
        EcoporiumUser user = plugin.getUserCache().getCache().get(player.getUniqueId()).join();

        // portfolio header
        plugin.getMessages().stockPortfolio.message(player);

        // run this async
        CompletableFuture.runAsync(() -> {
            user.getSharesOwnedTable().cellSet().forEach((cell) -> {
                String market = cell.getRowKey();
                String stock = cell.getColumnKey();

                // get current price per share

                // does market already exist?
                Market<?> marketObj = plugin.getMarketCache().getCache().get(market).join();

                // continue in stream by returning
                if (marketObj == null) {
                    return;
                }

                // does the stock exist?
                if (!marketObj.containsStock(stock)) {
                    // continue in stream by returning
                    return;
                }

                StockTicker<?> stockTicker = marketObj.getStock(stock);

                SimpleStockQuote quote = stockTicker.getCurrentQuote().orElse(null);

                // get messages
                Message stockPortfolioItemMessage = plugin.getMessages().stockPortfolioItem;

                if (quote == null) {
                    stockPortfolioItemMessage.message(player, "%market%", market, "%stock%", stock, "%shares-amount%", "Loading price...", "%position%", "");
                    return;
                }

                List<OwnedShare> ownedShares = cell.getValue();
                int totalOwnedShares = ownedShares.size();

                // calculate position percent (up or down)
                float positionSpent = ownedShares.stream().map(OwnedShare::getPriceOfEachShare).reduce(0.0f, Float::sum);

                String positionPercent = NumberUtil.formatToPlaces(Math.abs(((quote.getPrice() * totalOwnedShares / positionSpent) - 1) * 100), 2);
 
                // get position message
                Message positionMessage;

                if (quote.getPrice() * totalOwnedShares > positionSpent) {
                    positionMessage = plugin.getMessages().stockPortfolioPositionUp;
                } else if (quote.getPrice() * totalOwnedShares < positionSpent) {
                    positionMessage = plugin.getMessages().stockPortfolioPositionDown;
                } else {
                    positionMessage = plugin.getMessages().stockPortfolioPositionUnchanged;
                }

                stockPortfolioItemMessage.message(player, "%market%", market, "%stock%", stock, "%shares-amount%", Integer.toString(ownedShares.size()), "%position%", LegacyComponentSerializer.legacySection().serialize(positionMessage.compile("%percent%", positionPercent)));
            });
        });
    }

    @HelpCommand
    @Default
    @CatchUnknown
    public void doHelp(CommandSender sender, CommandHelp help) {
        plugin.getMessages().ecoporiumHelp.message(sender);
        help.showHelp();
    }

}
