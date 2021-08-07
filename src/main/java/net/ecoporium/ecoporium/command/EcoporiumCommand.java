package net.ecoporium.ecoporium.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.InvalidCommandArgument;
import co.aikar.commands.annotation.CatchUnknown;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.HelpCommand;
import co.aikar.commands.annotation.Single;
import co.aikar.commands.annotation.Split;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.annotation.Syntax;
import net.ecoporium.ecoporium.EcoporiumPlugin;
import net.ecoporium.ecoporium.api.message.Message;
import net.ecoporium.ecoporium.market.FakeMarket;
import net.ecoporium.ecoporium.market.Market;
import net.ecoporium.ecoporium.market.MarketCache;
import net.ecoporium.ecoporium.market.MarketType;
import net.ecoporium.ecoporium.market.MarketWhitelistOptions;
import net.ecoporium.ecoporium.market.RealMarket;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

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
        public void onMarketCreate(Player player, @Single String handle, @Syntax("<market type> - FAKE|REAL") @Single MarketType marketType) {
            MarketCache marketCache = plugin.getMarketCache();

            // does market already exist?
            Market marketPresent = marketCache.getCache().synchronous().getIfPresent(handle);

            if (marketPresent != null) {
                // exists already
                plugin.getMessages().marketExistsAlready.message(player);
                return;
            }

            Market market = null;

            // create new market
            if (marketType == MarketType.FAKE) {
                market = new FakeMarket(handle, new MarketWhitelistOptions(Collections.emptyList()), new HashMap<>());
            } else if (marketType == MarketType.REAL) {
                market = new RealMarket(handle, new MarketWhitelistOptions(Collections.emptyList()), new HashMap<>());
            } else {
                // something went wrong
                plugin.getMessages().somethingWentWrong.message(player);
                return;
            }

            // save market in storage, load into cache
            plugin.getStorage().saveMarket(market);
            plugin.getMarketCache().getCache().get(market.getHandle());
        }

        @Subcommand("delete")
        public void onMarketDelete(Player player, @Single String handle) {
        }

        @Subcommand("addstocks")
        public void onMarketAddStocks(Player player, @Single String handle, @Split(" ") String... symbols) {
        }

        @Subcommand("removestocks")
        public void onMarketRemoveStocks(Player player, @Single String handle, @Split(" ") String... symbols) {
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
