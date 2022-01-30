package com.yakovliam.ecoporium.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.InvalidCommandArgument;
import co.aikar.commands.annotation.*;
import com.yakovliam.ecoporium.EcoporiumPlugin;
import com.yakovliam.ecoporium.api.message.Message;
import com.yakovliam.ecoporium.api.wrapper.Pair;
import com.yakovliam.ecoporium.market.*;
import com.yakovliam.ecoporium.market.factory.FakeMarketFactory;
import com.yakovliam.ecoporium.market.factory.RealMarketFactory;
import com.yakovliam.ecoporium.market.stock.fake.FakeStockTicker;
import com.yakovliam.ecoporium.market.stock.fake.FakeStockTickerFactory;
import com.yakovliam.ecoporium.market.stock.real.RealStockTicker;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@CommandAlias("ecoporium")
@CommandPermission("ecoporium.command.ecoporium")
public class EcoporiumCommand extends AbstractEcoporiumCommand {

    /**
     * Ecoporium command
     *
     * @param manager manager
     * @param plugin  plugin
     */
    public EcoporiumCommand(CommandManager manager, EcoporiumPlugin plugin) {
        super(manager, plugin);
    }

    @Subcommand("reload")
    @CommandPermission("ecoporium.command.ecoporium.reload")
    @Description("Reloads configuration values")
    public void onReload(Player player) {
        // reload config
        plugin.getEcoporiumConfig().reload();
        plugin.getLangConfig().reload();
        plugin.loadMessages();

        plugin.getMessages().ecoporiumReloaded.message(player);
    }

    @CommandAlias("ecoporium")
    @Subcommand("market")
    public class MarketCommand extends BaseCommand {

        @Subcommand("create")
        @CommandPermission("ecoporium.command.ecoporium.market.create")
        public void onMarketCreate(Player player, @Single String market, @Syntax("<market type>") @Single MarketType marketType) {
            MarketCache marketCache = plugin.getMarketCache();

            // does market already exist?
            Market<?> marketPresent = marketCache.getCache().synchronous().getIfPresent(market);

            if (marketPresent != null) {
                // exists already
                plugin.getMessages().ecoporiumMarketExistsAlready.message(player);
                return;
            }

            Market<?> marketObj;

            // create new market
            if (marketType == MarketType.FAKE) {
                marketObj = new FakeMarketFactory().build(market);
            } else if (marketType == MarketType.REAL) {
                marketObj = new RealMarketFactory().build(market);
            } else {
                // something went wrong
                plugin.getMessages().somethingWentWrong.message(player);
                return;
            }

            // created message
            plugin.getMessages().ecoporiumMarketCreated.message(player);

            // save market in storage, load into cache (async)
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                plugin.getStorage().saveMarket(marketObj);
                plugin.getMarketCache().getCache().get(marketObj.getHandle());
            });
        }

        @Subcommand("delete")
        @CommandPermission("ecoporium.command.ecoporium.market.delete")
        public void onMarketDelete(Player player, @Single String market) {
            plugin.getMessages().ecoporiumMarketGettingData.message(player);

            // does market already exist?
            plugin.getMarketCache().getCache().get(market).thenAccept(marketObj -> {
                // if doesn't exist
                if (marketObj == null) {
                    plugin.getMessages().ecoporiumMarketNonexistent.message(player);
                    return;
                }

                // delete
                plugin.getStorage().deleteMarket(marketObj);
                // invalidate from cache
                plugin.getMarketCache().getCache().synchronous().invalidate(marketObj.getHandle());

                plugin.getMessages().ecoporiumMarketDeleted.message(player);
            });
        }

        @Subcommand("addstock")
        @CommandPermission("ecoporium.command.ecoporium.market.addstock")
        public void onMarketAddStock(Player player, @Single String market, @Single String symbol, @Optional String alias) {
            plugin.getMessages().ecoporiumMarketGettingData.message(player);

            // does market already exist?
            plugin.getMarketCache().getCache().get(market).thenAccept(marketObj -> {
                // if doesn't exist
                if (marketObj == null) {
                    plugin.getMessages().ecoporiumMarketNonexistent.message(player);
                    return;
                }

                // if fake
                if (marketObj.getMarketType() == MarketType.FAKE) {
                    FakeMarket fakeMarket = (FakeMarket) marketObj;
                    // if ticker already exists
                    if (fakeMarket.getTickerCache().containsKey(symbol)) {
                        plugin.getMessages().ecoporiumMarketSymbolAlreadyExists.message(player);
                        return;
                    }

                    // create new ticker
                    FakeStockTicker fakeStockTicker = new FakeStockTickerFactory().build(new Pair<>(symbol, alias));

                    // add to ticker cache
                    fakeMarket.getTickerCache().put(fakeStockTicker.getSymbol(), fakeStockTicker);
                } else if (marketObj.getMarketType() == MarketType.REAL) {
                    RealMarket realMarket = (RealMarket) marketObj;
                    // if ticker already exists
                    if (realMarket.getTickerCache().containsKey(symbol)) {
                        plugin.getMessages().ecoporiumMarketSymbolAlreadyExists.message(player);
                        return;
                    }

                    // create new ticker
                    RealStockTicker realStockTicker = new RealStockTicker(symbol);

                    // add to ticker cache
                    realMarket.getTickerCache().put(realStockTicker.getSymbol(), realStockTicker);
                } else {
                    plugin.getMessages().somethingWentWrong.message(player);
                    return;
                }

                // save market
                plugin.getStorage().saveMarket(marketObj);
                plugin.getMessages().ecoporiumMarketStockAdded.message(player);
            });
        }

        @Subcommand("removestock")
        @CommandPermission("ecoporium.command.ecoporium.market.removestock")
        public void onMarketRemoveStock(Player player, @Single String market, @Single String symbol) {
            plugin.getMessages().ecoporiumMarketGettingData.message(player);

            // does market already exist?
            plugin.getMarketCache().getCache().get(market).thenAccept(marketObj -> {
                // if doesn't exist
                if (marketObj == null) {
                    plugin.getMessages().ecoporiumMarketNonexistent.message(player);
                    return;
                }

                // if fake
                if (marketObj.getMarketType() == MarketType.FAKE) {
                    FakeMarket fakeMarket = (FakeMarket) marketObj;
                    // if ticker doesn't exist
                    if (!fakeMarket.getTickerCache().containsKey(symbol)) {
                        plugin.getMessages().ecoporiumMarketSymbolDoesntExist.message(player);
                        return;
                    }

                    // remove from cache
                    fakeMarket.getTickerCache().remove(symbol);
                } else if (marketObj.getMarketType() == MarketType.REAL) {
                    RealMarket realMarket = (RealMarket) marketObj;
                    // if ticker doesn't exist
                    if (!realMarket.getTickerCache().containsKey(symbol)) {
                        plugin.getMessages().ecoporiumMarketSymbolDoesntExist.message(player);
                        return;
                    }

                    // remove from cache
                    realMarket.getTickerCache().remove(symbol);
                } else {
                    plugin.getMessages().somethingWentWrong.message(player);
                    return;
                }

                // save market
                plugin.getStorage().saveMarket(marketObj);
                plugin.getMessages().ecoporiumMarketStockRemoved.message(player);
            });
        }

        @Subcommand("info")
        @CommandPermission("ecoporium.command.ecoporium.market.info")
        public void onMarketInfo(Player player, @Single String market) {
            plugin.getMessages().ecoporiumMarketGettingData.message(player);

            // does market already exist?
            plugin.getMarketCache().getCache().get(market).thenAccept(marketObj -> {
                // if doesn't exist
                if (marketObj == null) {
                    plugin.getMessages().ecoporiumMarketNonexistent.message(player);
                    return;
                }

                // get all tickers
                List<String> symbols = new ArrayList<>();

                if (marketObj.getMarketType() == MarketType.FAKE) {
                    ((FakeMarket) marketObj).getTickerCache().values().forEach(t -> symbols.add(t.getSymbol()));
                } else if (marketObj.getMarketType() == MarketType.REAL) {
                    ((RealMarket) marketObj).getTickerCache().values().forEach(t -> symbols.add(t.getSymbol()));
                } else {
                    plugin.getMessages().somethingWentWrong.message(player);
                    return;
                }

                Message.Builder builder = Message.builder()
                        .addLine("&7Market info (&f" + marketObj.getHandle() + "&7):")
                        .addLine("&7Symbols: &f" + symbols.size());
                symbols.forEach(s -> builder.addLine("&7- &f" + s));
                builder.build().message(player);
            });
        }
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
        manager.getCommandContexts().registerIssuerAwareContext(MarketType.class, c -> {
            String s = c.popFirstArg();

            if (s == null) {
                return null;
            }

            MarketType m;
            try {
                m = MarketType.valueOf(s);
            } catch (IllegalArgumentException ignored) {
                throw new InvalidCommandArgument("Invalid market type!");
            }

            return m;
        });
    }
}
