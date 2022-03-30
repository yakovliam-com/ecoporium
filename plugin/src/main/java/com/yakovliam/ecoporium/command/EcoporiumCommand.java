package com.yakovliam.ecoporium.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.InvalidCommandArgument;
import co.aikar.commands.annotation.*;
import com.yakovliam.ecoporium.EcoporiumPlugin;
import com.yakovliam.ecoporium.market.FakeMarketImpl;
import com.yakovliam.ecoporium.api.market.Market;
import com.yakovliam.ecoporium.api.market.MarketType;
import com.yakovliam.ecoporium.market.RealMarketImpl;
import com.yakovliam.ecoporium.api.message.Message;
import com.yakovliam.ecoporium.api.wrapper.Pair;
import com.yakovliam.ecoporium.market.*;
import com.yakovliam.ecoporium.market.factory.FakeMarketFactory;
import com.yakovliam.ecoporium.market.factory.RealMarketFactory;
import com.yakovliam.ecoporium.market.stock.fake.FakeStockTickerImpl;
import com.yakovliam.ecoporium.market.stock.fake.FakeStockTickerFactory;
import com.yakovliam.ecoporium.market.stock.real.RealStockTickerFactory;
import com.yakovliam.ecoporium.market.stock.real.RealStockTickerImpl;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
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
        public void onMarketCreate(CommandSender sender, @Single String market, @Syntax("<market type>") @Single MarketType marketType) {
            MarketCacheImpl marketCacheImpl = plugin.getMarketCache();

            // does market already exist?
            Market<?> marketPresent = marketCacheImpl.getCache().synchronous().getIfPresent(market);

            if (marketPresent != null) {
                // exists already
                plugin.getMessages().ecoporiumMarketExistsAlready.message(sender);
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
                plugin.getMessages().somethingWentWrong.message(sender);
                plugin.getMessages().somethingWentWrong.broadcast();
                System.out.println(LegacyComponentSerializer.builder().build().serialize(plugin.getMessages().somethingWentWrong.compile()));
                System.out.println("IN ELSE");
                return;
            }

            // created message
            plugin.getMessages().ecoporiumMarketCreated.message(sender);

            // save market in storage, load into cache (async)
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                plugin.getStorage().saveMarket(marketObj);
                plugin.getMarketCache().getCache().get(marketObj.getHandle());
            });
        }

        @Subcommand("delete")
        @CommandPermission("ecoporium.command.ecoporium.market.delete")
        public void onMarketDelete(CommandSender sender, @Single String market) {
            plugin.getMessages().ecoporiumMarketGettingData.message(sender);

            // does market already exist?
            plugin.getMarketCache().getCache().get(market).thenAccept(marketObj -> {
                // if doesn't exist
                if (marketObj == null) {
                    plugin.getMessages().ecoporiumMarketNonexistent.message(sender);
                    return;
                }

                // delete
                plugin.getStorage().deleteMarket(marketObj);
                // invalidate from cache
                plugin.getMarketCache().getCache().synchronous().invalidate(marketObj.getHandle());

                plugin.getMessages().ecoporiumMarketDeleted.message(sender);
            });
        }

        @Subcommand("addstock")
        @CommandPermission("ecoporium.command.ecoporium.market.addstock")
        public void onMarketAddStock(CommandSender sender, @Single String market, @Single String symbol, @Optional String alias) {
            plugin.getMessages().ecoporiumMarketGettingData.message(sender);

            // does market already exist?
            plugin.getMarketCache().getCache().get(market).thenAccept(marketObj -> {
                // if doesn't exist
                if (marketObj == null) {
                    plugin.getMessages().ecoporiumMarketNonexistent.message(sender);
                    return;
                }

                // if fake
                if (marketObj.getMarketType() == MarketType.FAKE) {
                    FakeMarketImpl fakeMarketImpl = (FakeMarketImpl) marketObj;
                    // if ticker already exists
                    if (fakeMarketImpl.getTickerCache().containsKey(symbol)) {
                        plugin.getMessages().ecoporiumMarketSymbolAlreadyExists.message(sender);
                        return;
                    }

                    // create new ticker
                    FakeStockTickerImpl fakeStockTickerImpl = new FakeStockTickerFactory().build(new Pair<>(symbol, alias));

                    // add to ticker cache
                    fakeMarketImpl.getTickerCache().put(fakeStockTickerImpl.getSymbol(), fakeStockTickerImpl);
                } else if (marketObj.getMarketType() == MarketType.REAL) {
                    RealMarketImpl realMarketImpl = (RealMarketImpl) marketObj;
                    // if ticker already exists
                    if (realMarketImpl.getTickerCache().containsKey(symbol)) {
                        plugin.getMessages().ecoporiumMarketSymbolAlreadyExists.message(sender);
                        return;
                    }

                    // create new ticker
                    RealStockTickerImpl realStockTickerImpl = new RealStockTickerFactory().build(symbol);

                    // add to ticker cache
                    realMarketImpl.getTickerCache().put(realStockTickerImpl.getSymbol(), realStockTickerImpl);
                } else {
                    plugin.getMessages().somethingWentWrong.message(sender);
                    return;
                }

                // save market
                plugin.getStorage().saveMarket(marketObj);
                plugin.getMessages().ecoporiumMarketStockAdded.message(sender);
            });
        }

        @Subcommand("removestock")
        @CommandPermission("ecoporium.command.ecoporium.market.removestock")
        public void onMarketRemoveStock(CommandSender sender, @Single String market, @Single String symbol) {
            plugin.getMessages().ecoporiumMarketGettingData.message(sender);

            // does market already exist?
            plugin.getMarketCache().getCache().get(market).thenAccept(marketObj -> {
                // if doesn't exist
                if (marketObj == null) {
                    plugin.getMessages().ecoporiumMarketNonexistent.message(sender);
                    return;
                }

                // if fake
                if (marketObj.getMarketType() == MarketType.FAKE) {
                    FakeMarketImpl fakeMarketImpl = (FakeMarketImpl) marketObj;
                    // if ticker doesn't exist
                    if (!fakeMarketImpl.getTickerCache().containsKey(symbol)) {
                        plugin.getMessages().ecoporiumMarketSymbolDoesntExist.message(sender);
                        return;
                    }

                    // remove from cache
                    fakeMarketImpl.getTickerCache().remove(symbol);
                } else if (marketObj.getMarketType() == MarketType.REAL) {
                    RealMarketImpl realMarketImpl = (RealMarketImpl) marketObj;
                    // if ticker doesn't exist
                    if (!realMarketImpl.getTickerCache().containsKey(symbol)) {
                        plugin.getMessages().ecoporiumMarketSymbolDoesntExist.message(sender);
                        return;
                    }

                    // remove from cache
                    realMarketImpl.getTickerCache().remove(symbol);
                } else {
                    plugin.getMessages().somethingWentWrong.message(sender);
                    return;
                }

                // save market
                plugin.getStorage().saveMarket(marketObj);
                plugin.getMessages().ecoporiumMarketStockRemoved.message(sender);
            });
        }

        @Subcommand("info")
        @CommandPermission("ecoporium.command.ecoporium.market.info")
        public void onMarketInfo(CommandSender sender, @Single String market) {
            plugin.getMessages().ecoporiumMarketGettingData.message(sender);

            // does market already exist?
            plugin.getMarketCache().getCache().get(market).thenAccept(marketObj -> {
                // if doesn't exist
                if (marketObj == null) {
                    plugin.getMessages().ecoporiumMarketNonexistent.message(sender);
                    return;
                }

                // get all tickers
                List<String> symbols = new ArrayList<>();

                if (marketObj.getMarketType() == MarketType.FAKE) {
                    ((FakeMarketImpl) marketObj).getTickerCache().values().forEach(t -> symbols.add(t.getSymbol()));
                } else if (marketObj.getMarketType() == MarketType.REAL) {
                    ((RealMarketImpl) marketObj).getTickerCache().values().forEach(t -> symbols.add(t.getSymbol()));
                } else {
                    plugin.getMessages().somethingWentWrong.message(sender);
                    return;
                }

                Message.Builder builder = Message.builder()
                        .addLine("&7Market info (&f" + marketObj.getHandle() + "&7):")
                        .addLine("&7Symbols: &f" + symbols.size());
                symbols.forEach(s -> builder.addLine("&7- &f" + s));
                builder.build().message(sender);
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
