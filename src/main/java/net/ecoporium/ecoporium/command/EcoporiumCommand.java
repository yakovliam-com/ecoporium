package net.ecoporium.ecoporium.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.InvalidCommandArgument;
import co.aikar.commands.annotation.CatchUnknown;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.HelpCommand;
import co.aikar.commands.annotation.Optional;
import co.aikar.commands.annotation.Single;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.annotation.Syntax;
import com.github.johnnyjayjay.spigotmaps.RenderedMap;
import com.github.johnnyjayjay.spigotmaps.util.Compatibility;
import net.ecoporium.ecoporium.EcoporiumPlugin;
import net.ecoporium.ecoporium.api.message.Message;
import net.ecoporium.ecoporium.api.wrapper.Pair;
import net.ecoporium.ecoporium.market.FakeMarket;
import net.ecoporium.ecoporium.market.Market;
import net.ecoporium.ecoporium.market.MarketCache;
import net.ecoporium.ecoporium.market.MarketType;
import net.ecoporium.ecoporium.market.RealMarket;
import net.ecoporium.ecoporium.market.factory.FakeMarketFactory;
import net.ecoporium.ecoporium.market.factory.RealMarketFactory;
import net.ecoporium.ecoporium.market.stock.StockTicker;
import net.ecoporium.ecoporium.market.stock.fake.FakeStockTicker;
import net.ecoporium.ecoporium.market.stock.real.RealStockTicker;
import net.ecoporium.ecoporium.market.stock.fake.FakeStockTickerFactory;
import net.ecoporium.ecoporium.screen.TrendScreen;
import net.ecoporium.ecoporium.screen.info.MapInfo;
import net.ecoporium.ecoporium.screen.info.ScreenInfo;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
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

    @Subcommand("test")
    public void onTest(Player player, @Single String market, @Single String symbol) {
        plugin.getMessages().retrievingMarket.message(player);

        // does market already exist?
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

            // create maps
            MapView singleMapView = Bukkit.createMap(player.getWorld());

            // give to player
            player.getInventory().addItem(createItemStack(singleMapView, "Ticker screen for " + stockTicker.getSymbol()));
            player.updateInventory();

            // create trend screen
            TrendScreen screen = new TrendScreen(marketObj, stockTicker, new ScreenInfo(128, 128), new MapInfo(Collections.singletonList(singleMapView.getId())));
            screen.startScreen(plugin);
        });
    }

    private ItemStack createItemStack(MapView mapView, String displayName, String... lore) {
        ItemStack itemStack = new ItemStack(Compatibility.isLegacy() ? Material.MAP : Material.FILLED_MAP);
        MapMeta mapMeta = (MapMeta) itemStack.getItemMeta();
        mapMeta.setMapView(mapView);
        mapMeta.setDisplayName(displayName);
        mapMeta.setLore(lore.length == 0 ? null : Arrays.asList(lore));
        itemStack.setItemMeta(mapMeta);
        return itemStack;
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
