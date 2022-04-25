package com.yakovliam.ecoporium.command;

import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import com.yakovliam.ecoporium.EcoporiumPlugin;
import com.yakovliam.ecoporium.api.market.Market;
import com.yakovliam.ecoporium.api.market.stock.StockTicker;
import com.yakovliam.ecoporium.api.market.stock.quote.SimpleStockQuote;
import com.yakovliam.ecoporium.api.user.EcoporiumUser;
import com.yakovliam.ecoporium.api.user.share.OwnedShare;
import com.yakovliam.ecoporium.user.EcoporiumUserImpl;
import com.yakovliam.ecoporium.util.NumberUtil;
import net.kyori.adventure.text.Component;
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
        EcoporiumPlugin.audiences().player(player).sendMessage(plugin.configSupervisor().messages().ecoporiumMarketGettingData());

        // does market already exist?
        plugin.marketCache().cache().get(market).thenAccept(marketObj -> {
            // if doesn't exist
            if (marketObj == null) {
                EcoporiumPlugin.audiences().player(player).sendMessage(plugin.configSupervisor().messages().ecoporiumMarketNonexistent());
                return;
            }

            float pricePerShare;

            // does the stock exist?
            if (!marketObj.containsStock(symbol)) {
                EcoporiumPlugin.audiences().player(player).sendMessage(plugin.configSupervisor().messages().ecoporiumMarketSymbolDoesntExist());
                return;
            }

            StockTicker<?> stockTicker = marketObj.stock(symbol);
            if (stockTicker.getCurrentQuote().isEmpty()) {
                EcoporiumPlugin.audiences().player(player).sendMessage(plugin.configSupervisor().messages().stockPriceNotAvailable());
                return;
            }

            pricePerShare = stockTicker.getCurrentQuote().get().price();

            // if the user has enough to pay for the stocks they are buying
            float amountNeededToBuy = pricePerShare * amountToBuy;
            float balance = (float) plugin.economy().getBalance(player);

            if (balance < amountNeededToBuy) {
                EcoporiumPlugin.audiences().player(player).sendMessage(plugin.configSupervisor().messages().stockBuyNotEnough().replaceText((builder ->
                        builder.matchLiteral("%balance-needed%").replacement(NumberUtil.formatToPlaces(amountNeededToBuy, 2)))));
                return;
            }

            // get user
            EcoporiumUserImpl user = plugin.userCache().cache().get(player.getUniqueId()).join();

            // give user shares
            user.addShares(marketObj.handle(), stockTicker.getSymbol(), amountToBuy, pricePerShare);
            // save user
            plugin.storage().saveUser(user, true);

            // withdraw
            plugin.economy().withdrawPlayer(player, amountNeededToBuy);

            EcoporiumPlugin.audiences().player(player).sendMessage(plugin.configSupervisor().messages().stockBuyBought()
                    .replaceText((b) -> b.matchLiteral("%shares%").replacement(Integer.toString(amountToBuy)))
                    .replaceText(b -> b.matchLiteral("%symbol%").replacement(symbol))
                    .replaceText(b -> b.matchLiteral("%price-per-share%").replacement(NumberUtil.formatToPlaces(pricePerShare, 2)))
                    .replaceText(b -> b.matchLiteral("%amount-paid%").replacement(NumberUtil.formatToPlaces(amountNeededToBuy, 2))));
        });
    }

    @Subcommand("sell|s")
    @Description("Sells a stock from a particular market")
    public void onSell(Player player, @Single String market, @Single String symbol, @Single Integer amountToSell) {
        EcoporiumPlugin.audiences().player(player).sendMessage(plugin.configSupervisor().messages().ecoporiumMarketGettingData());

        // does market already exist?
        plugin.marketCache().cache().get(market).thenAccept(marketObj -> {
            // if doesn't exist
            if (marketObj == null) {
                EcoporiumPlugin.audiences().player(player).sendMessage(plugin.configSupervisor().messages().ecoporiumMarketNonexistent());
                return;
            }

            float pricePerShare;

            // does the stock exist?
            if (!marketObj.containsStock(symbol)) {
                EcoporiumPlugin.audiences().player(player).sendMessage(plugin.configSupervisor().messages().ecoporiumMarketSymbolDoesntExist());
                return;
            }

            StockTicker<?> stockTicker = marketObj.stock(symbol);

            if (stockTicker.getCurrentQuote().isEmpty()) {
                EcoporiumPlugin.audiences().player(player).sendMessage(plugin.configSupervisor().messages().stockPriceNotAvailable());
                return;
            }

            pricePerShare = stockTicker.getCurrentQuote().get().price();

            // get user
            EcoporiumUserImpl user = plugin.userCache().cache().get(player.getUniqueId()).join();

            // if user doesn't have enough shares
            int sharesOwned = user.numberOfShares(marketObj.handle(), stockTicker.getSymbol());

            if (sharesOwned < amountToSell) {
                EcoporiumPlugin.audiences().player(player).sendMessage(plugin.configSupervisor().messages().stockSellNotEnough());
                return;
            }

            // calculate amount to give player
            float amountToGive = pricePerShare * amountToSell;
            // deposit
            plugin.economy().depositPlayer(player, amountToGive);

            // remove user shares
            user.removeShares(marketObj.handle(), stockTicker.getSymbol(), amountToSell);
            // save user
            plugin.storage().saveUser(user, true);

            EcoporiumPlugin.audiences().player(player).sendMessage(plugin.configSupervisor().messages().stockSellSold()
                    .replaceText((b) -> b.matchLiteral("%shares%").replacement(amountToSell.toString()))
                    .replaceText(b -> b.matchLiteral("%symbol%").replacement(stockTicker.getSymbol()))
                    .replaceText(b -> b.matchLiteral("%price-per-share%").replacement(NumberUtil.formatToPlaces(pricePerShare, 2)))
                    .replaceText(b -> b.matchLiteral("%amount-given%").replacement(NumberUtil.formatToPlaces(amountToGive, 2))));
        });
    }

    @Subcommand("price|pr")
    @Description("Views a stock's price")
    public void onPrice(CommandSender sender, @Single String market, @Single String symbol) {
        EcoporiumPlugin.audiences().sender(sender).sendMessage(plugin.configSupervisor().messages().ecoporiumMarketGettingData());

        // does market already exist?
        plugin.marketCache().cache().get(market).thenAccept(marketObj -> {
            // if doesn't exist
            if (marketObj == null) {
                EcoporiumPlugin.audiences().sender(sender).sendMessage(plugin.configSupervisor().messages().ecoporiumMarketNonexistent());
                return;
            }

            float pricePerShare;

            // does the stock exist?
            if (!marketObj.containsStock(symbol)) {
                EcoporiumPlugin.audiences().sender(sender).sendMessage(plugin.configSupervisor().messages().ecoporiumMarketSymbolDoesntExist());
                return;
            }

            StockTicker<?> stockTicker = marketObj.stock(symbol);
            stockTicker.getCurrentQuote().ifPresentOrElse((quote) -> {
                EcoporiumPlugin.audiences().sender(sender).sendMessage(plugin.configSupervisor().messages().stockPrice()
                        .replaceText(builder -> builder.matchLiteral("%symbol%").replacement(stockTicker.getSymbol()))
                        .replaceText(builder -> builder.matchLiteral("%price-per-share%").replacement(NumberUtil.formatToPlaces(quote.price(), 2))));
            }, () -> {
                EcoporiumPlugin.audiences().sender(sender).sendMessage(plugin.configSupervisor().messages().stockPriceNotAvailable());
            });
        });
    }

    @Subcommand("portfolio|po")
    @Description("Views your portfolio")
    public void onPortfolio(Player player) {
        // get user
        EcoporiumUser user = plugin.userCache().cache().get(player.getUniqueId()).join();

        // portfolio header
        EcoporiumPlugin.audiences().player(player).sendMessage(plugin.configSupervisor().messages().stockPortfolio());

        // run this async
        CompletableFuture.runAsync(() -> {
            user.sharesOwnedTable().cellSet().forEach((cell) -> {
                String market = cell.getRowKey();
                String stock = cell.getColumnKey();

                // get current price per share

                // does market already exist?
                Market<?> marketObj = plugin.marketCache().cache().get(market).join();

                // continue in stream by returning
                if (marketObj == null) {
                    return;
                }

                // does the stock exist?
                if (!marketObj.containsStock(stock)) {
                    // continue in stream by returning
                    return;
                }

                StockTicker<?> stockTicker = marketObj.stock(stock);

                SimpleStockQuote quote = stockTicker.getCurrentQuote().orElse(null);

                if (quote == null) {
                    EcoporiumPlugin.audiences().player(player).sendMessage(plugin.configSupervisor().messages().stockPortfolioItem().replaceText(b -> b.matchLiteral("%market%").replacement(market).matchLiteral("%stock%").replacement(stock).matchLiteral("%shares-amount%").replacement("Loading price...").matchLiteral("%position%").replacement("")));
                    return;
                }

                List<OwnedShare> ownedShares = cell.getValue();
                int totalOwnedShares = ownedShares.size();

                // calculate position percent (up or down)
                float positionSpent = ownedShares.stream().map(OwnedShare::priceOfEachShare).reduce(0.0f, Float::sum);

                String positionPercent = NumberUtil.formatToPlaces(Math.abs(((quote.price() * totalOwnedShares / positionSpent) - 1) * 100), 2);

                // get position message
                Component positionComponent;

                if (quote.price() * totalOwnedShares > positionSpent) {
                    positionComponent = plugin.configSupervisor().messages().stockPortfolioPositionUp();
                } else if (quote.price() * totalOwnedShares < positionSpent) {
                    positionComponent = plugin.configSupervisor().messages().stockPortfolioPositionDown();
                } else {
                    positionComponent = plugin.configSupervisor().messages().stockPortfolioPositionUnchanged();
                }

                EcoporiumPlugin.audiences().player(player).sendMessage(positionComponent
                        .replaceText(b -> b.matchLiteral("%market%").replacement(market))
                        .replaceText(b -> b.matchLiteral("%stock%").replacement(stock))
                        .replaceText(b -> b.matchLiteral("%shares-amount%").replacement(Integer.toString(ownedShares.size())))
                        .replaceText(b -> b.matchLiteral("%position%").replacement(LegacyComponentSerializer.legacySection().serialize(positionComponent.replaceText(b2 -> b2.matchLiteral("%percent%").replacement(positionPercent))))));
            });
        });
    }

    @HelpCommand
    @Default
    @CatchUnknown
    public void doHelp(CommandSender sender, CommandHelp help) {
        EcoporiumPlugin.audiences().sender(sender).sendMessage(plugin.configSupervisor().messages().ecoporiumHelp());
        help.showHelp();
    }
}
