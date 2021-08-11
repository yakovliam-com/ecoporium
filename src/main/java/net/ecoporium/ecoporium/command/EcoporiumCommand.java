package net.ecoporium.ecoporium.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.InvalidCommandArgument;
import co.aikar.commands.annotation.Optional;
import co.aikar.commands.annotation.*;
import net.ecoporium.ecoporium.EcoporiumPlugin;
import net.ecoporium.ecoporium.api.message.Message;
import net.ecoporium.ecoporium.api.wrapper.Pair;
import net.ecoporium.ecoporium.market.*;
import net.ecoporium.ecoporium.market.factory.FakeMarketFactory;
import net.ecoporium.ecoporium.market.factory.RealMarketFactory;
import net.ecoporium.ecoporium.market.stock.StockTicker;
import net.ecoporium.ecoporium.market.stock.fake.FakeStockTicker;
import net.ecoporium.ecoporium.market.stock.fake.FakeStockTickerFactory;
import net.ecoporium.ecoporium.market.stock.real.RealStockTicker;
import net.ecoporium.ecoporium.screen.TrendScreen;
import net.ecoporium.ecoporium.screen.info.ScreenInfo;
import net.ecoporium.ecoporium.util.ScreenCalculationUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;

import java.util.*;

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

        plugin.getMessages().reloaded.message(player);
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
                plugin.getMessages().marketExistsAlready.message(player);
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
            plugin.getMessages().marketCreated.message(player);

            // save market in storage, load into cache (async)
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                plugin.getStorage().saveMarket(marketObj);
                plugin.getMarketCache().getCache().get(marketObj.getHandle());
            });
        }

        @Subcommand("delete")
        @CommandPermission("ecoporium.command.ecoporium.market.delete")
        public void onMarketDelete(Player player, @Single String market) {
            plugin.getMessages().retrievingMarket.message(player);

            // does market already exist?
            plugin.getMarketCache().getCache().get(market).thenAccept(marketObj -> {
                // if doesn't exist
                if (marketObj == null) {
                    plugin.getMessages().marketNonexistent.message(player);
                    return;
                }

                // delete
                plugin.getStorage().deleteMarket(marketObj);
                // invalidate from cache
                plugin.getMarketCache().getCache().synchronous().invalidate(marketObj.getHandle());

                plugin.getMessages().marketDeleted.message(player);
            });
        }

        @Subcommand("addstock")
        @CommandPermission("ecoporium.command.ecoporium.market.addstock")
        public void onMarketAddStock(Player player, @Single String market, @Single String symbol, @Optional String alias) {
            plugin.getMessages().retrievingMarket.message(player);

            // does market already exist?
            plugin.getMarketCache().getCache().get(market).thenAccept(marketObj -> {
                // if doesn't exist
                if (marketObj == null) {
                    plugin.getMessages().marketNonexistent.message(player);
                    return;
                }

                // if fake
                if (marketObj.getMarketType() == MarketType.FAKE) {
                    FakeMarket fakeMarket = (FakeMarket) marketObj;
                    // if ticker already exists
                    if (fakeMarket.getTickerCache().containsKey(symbol)) {
                        plugin.getMessages().marketSymbolAlreadyExists.message(player);
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
                        plugin.getMessages().marketSymbolAlreadyExists.message(player);
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
                plugin.getMessages().stockAdded.message(player);
            });
        }

        @Subcommand("removestock")
        @CommandPermission("ecoporium.command.ecoporium.market.removestock")
        public void onMarketRemoveStock(Player player, @Single String market, @Single String symbol) {
            plugin.getMessages().retrievingMarket.message(player);

            // does market already exist?
            plugin.getMarketCache().getCache().get(market).thenAccept(marketObj -> {
                // if doesn't exist
                if (marketObj == null) {
                    plugin.getMessages().marketNonexistent.message(player);
                    return;
                }

                // if fake
                if (marketObj.getMarketType() == MarketType.FAKE) {
                    FakeMarket fakeMarket = (FakeMarket) marketObj;
                    // if ticker doesn't exist
                    if (!fakeMarket.getTickerCache().containsKey(symbol)) {
                        plugin.getMessages().marketSymbolDoesntExist.message(player);
                        return;
                    }

                    // remove from cache
                    fakeMarket.getTickerCache().remove(symbol);
                } else if (marketObj.getMarketType() == MarketType.REAL) {
                    RealMarket realMarket = (RealMarket) marketObj;
                    // if ticker doesn't exist
                    if (!realMarket.getTickerCache().containsKey(symbol)) {
                        plugin.getMessages().marketSymbolDoesntExist.message(player);
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
                plugin.getMessages().stockRemoved.message(player);
            });
        }

        @Subcommand("info")
        @CommandPermission("ecoporium.command.ecoporium.market.info")
        public void onMarketInfo(Player player, @Single String market) {
            plugin.getMessages().retrievingMarket.message(player);

            // does market already exist?
            plugin.getMarketCache().getCache().get(market).thenAccept(marketObj -> {
                // if doesn't exist
                if (marketObj == null) {
                    plugin.getMessages().marketNonexistent.message(player);
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

    @CommandAlias("ecoporium")
    @Subcommand("screen")
    public class ScreenCommand extends BaseCommand {

        @Subcommand("create")
        @Description("Creates a trend screen through a placement session")
        @CommandPermission("ecoporium.command.ecoporium.screen.create")
        public void onCreate(Player player, @Single String market, @Single String symbol, @Syntax("EXAMPLES: 5x5 or 7x7") @Single String dimensions) {
            plugin.getMessages().retrievingMarket.message(player);

            // if already in a session
            if (plugin.getMapPlacementHandler().getPlayerItemPlaceQueue().containsKey(player.getUniqueId())) {
                plugin.getMessages().screenCreateAlreadyInSession.message(player);
            }

            // does the market exist?
            plugin.getMarketCache().getCache().get(market).thenAccept(marketObj -> {
                // if doesn't exist
                if (marketObj == null) {
                    plugin.getMessages().marketNonexistent.message(player);
                    return;
                }

                // does the stock exist?
                if (!marketObj.containsStock(symbol)) {
                    plugin.getMessages().marketSymbolDoesntExist.message(player);
                    return;
                }

                StockTicker<?> stockTicker = marketObj.getStock(symbol);

                // calculate dimensions
                Pair<Integer, Integer> dimensionsPair = ScreenCalculationUtil.parseDimensions(dimensions);

                if (dimensionsPair == null) {
                    // oops!
                    plugin.getMessages().somethingWentWrong.message(player);
                    return;
                }

                // parse into screen info
                ScreenInfo screenInfo = ScreenCalculationUtil.constructFromMapSizeDimensions(dimensionsPair);

                // call map placement handler to create screen
                plugin.getMapPlacementHandler().createScreen(player, marketObj, stockTicker, screenInfo);
            });
        }

        @Subcommand("cancelsession")
        @Description("Cancels an ongoing creation session")
        @CommandPermission("ecoporium.command.ecoporium.screen.cancel")
        public void onCancelSession(Player player) {
            Pair<UUID, LinkedList<ItemStack>> queue = plugin.getMapPlacementHandler().getPlayerItemPlaceQueue().get(player.getUniqueId());

            if (queue == null) {
                plugin.getMessages().screenCreateCancelNotInPlacementSession.message(player);
                return;
            }

            // remove from handler
            plugin.getMapPlacementHandler().getPlayerItemPlaceQueue().remove(player.getUniqueId());

            TrendScreen trendScreen = plugin.getTrendScreenManager().getByUUID(queue.getLeft());
            // remove from manager
            plugin.getTrendScreenManager().removeTrendScreen(trendScreen);
            // remove from storage
            plugin.getStorage().deleteTrendScreen(trendScreen);

            // message
            plugin.getMessages().screenCreateCanceled.message(player);
        }

        @Subcommand("delete")
        @Description("Deletes a trend screen that the player is looking at")
        @CommandPermission("ecoporium.command.ecoporium.screen.delete")
        public void onDelete(Player player) {
            // get point the player is looking at
            Block targetBlock = player.getTargetBlockExact(15);

            // get entities nearby, see if item frame
            MapView mapView = Objects.requireNonNull(Objects.requireNonNull(targetBlock).getLocation().getWorld()).getNearbyEntities(targetBlock.getLocation(), 5, 5, 5).stream()
                    .filter(e -> e instanceof ItemFrame)
                    .map(e -> (ItemFrame) e)
                    .filter(i -> i.getItem().getType().equals(Material.FILLED_MAP))
                    .map(i -> ((MapMeta) Objects.requireNonNull(i.getItem().getItemMeta())).getMapView())
                    .findFirst()
                    .orElse(null);

            if (mapView == null) {
                // can't find screen
                plugin.getMessages().screenCantFind.message(player);
                return;
            }

            // get trend screen
            TrendScreen trendScreen = plugin.getTrendScreenManager().getByMapId(mapView.getId());

            if (trendScreen == null) {
                plugin.getMessages().screenCantFind.message(player);
                return;
            }

            // delete / remove
            trendScreen.stopScreen();
            plugin.getTrendScreenManager().removeTrendScreen(trendScreen);
            plugin.getStorage().deleteTrendScreen(trendScreen);

            plugin.getMessages().screenDeleted.message(player);
        }
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
