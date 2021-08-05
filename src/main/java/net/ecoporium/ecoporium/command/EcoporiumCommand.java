package net.ecoporium.ecoporium.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.BukkitCommandCompletionContext;
import co.aikar.commands.CommandCompletions;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.InvalidCommandArgument;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.HelpCommand;
import co.aikar.commands.annotation.Single;
import co.aikar.commands.annotation.Subcommand;
import net.ecoporium.ecoporium.EcoporiumPlugin;
import net.ecoporium.ecoporium.api.message.Message;
import net.ecoporium.ecoporium.api.wrapper.Pair;
import net.ecoporium.ecoporium.model.market.Market;
import net.ecoporium.ecoporium.screen.TickerScreen;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.LinkedList;
import java.util.UUID;
import java.util.stream.Collectors;

@CommandAlias("ecoporium")
@CommandPermission("ecoporium.command")
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
    @Subcommand("create")
    @CommandPermission("ecoporium.command.create")
    public class CreateCommand extends BaseCommand {

        @Subcommand("static")
        @CommandPermission("ecoporium.command.create.screen.static")
        public void onCreateStatic(Player player, @Single Market market, @Single String symbol) {
            // if market doesn't have in whitelist
            if (market.getWhitelistOptions().getTickers().size() >= 1 && !market.getWhitelistOptions().getTickers().contains(symbol)) {
                plugin.getMessages().ecoporiumCreateStaticWhitelist.message(player);
                return;
            }

            // start screen creation
            plugin.getMapPlacementHandler().createScreen(player, market, symbol);

            plugin.getMessages().ecoporiumCreateStaticWaiting.message(player, "%symbol%", symbol);
        }

        @Subcommand("cancel")
        @CommandPermission("ecoporium.command.create.screen.cancel")
        public void onCancel(Player player) {
            Pair<UUID, LinkedList<ItemStack>> queue = plugin.getMapPlacementHandler().getPlayerItemPlaceQueue().get(player.getUniqueId());

            if (queue == null) {
                plugin.getMessages().ecoporiumCreateCancelNotInPlacementSession.message(player);
                return;
            }

            // remove from handler
            plugin.getMapPlacementHandler().getPlayerItemPlaceQueue().remove(player.getUniqueId());

            TickerScreen tickerScreen = plugin.getTickerScreenManager().get(queue.getLeft());
            // remove from manager
            plugin.getTickerScreenManager().removeScreen(tickerScreen.getId());
            // remove from storage
            plugin.getStorage().deleteTickerScreen(tickerScreen);

            plugin.getMessages().ecoporiumCreateCanceled.message(player);
        }
    }

    @CommandAlias("ecoporium")
    @Subcommand("delete")
    @CommandPermission("ecoporium.command.delete")
    public class DeleteCommand extends BaseCommand {

        @Default
        public void onDelete(Player player, @Single TickerScreen tickerScreen) {
            // remove from storage
            plugin.getStorage().deleteTickerScreen(tickerScreen);
            // remove from manager
            plugin.getTickerScreenManager().removeScreen(tickerScreen.getId());

            plugin.getMessages().ecoporiumDeleteDeleted.message(player);
        }
    }

    @HelpCommand
    @Default
    public void doHelp(CommandSender sender, CommandHelp help) {
        Message.builder("help")
                .addLine("&eEcoporium Help")
                .build()
                .message(sender);

        help.showHelp();
    }

    @Override
    protected void registerCompletions() {
        CommandCompletions<BukkitCommandCompletionContext> commandCompletions = manager.getCommandCompletions();

        commandCompletions.registerCompletion("market", c -> plugin.getMarketCache().getMap().values().stream()
                .map(Market::getHandle)
                .collect(Collectors.toList()));
    }

    @Override
    protected void registerContexts() {
        manager.getCommandContexts().registerIssuerAwareContext(Market.class, c -> {
            String firstArg = c.popFirstArg();

            if (firstArg == null) {
                throw new InvalidCommandArgument("Invalid market name provided");
            }

            Market market = plugin.getMarketCache().get(firstArg, null);

            if (market == null) {
                Message.builder()
                        .addLine("&cOops! We couldn't find any information on the market called &f" + firstArg + "&c.")
                        .build()
                        .message(c.getSender());
                throw new InvalidCommandArgument("Invalid market name provided");
            }

            return market;
        });

        manager.getCommandContexts().registerIssuerAwareContext(TickerScreen.class, c -> {
            String firstArg = c.popFirstArg();

            if (firstArg == null) {
                throw new InvalidCommandArgument("Invalid Screen UUID/ID provided");
            }

            // convert to uuid
            UUID uuid;
            try {
                uuid = UUID.fromString(firstArg);
            } catch (IllegalArgumentException exception) {
                throw new InvalidCommandArgument("Invalid Screen UUID/ID provided");
            }

            // get ticker screen
            TickerScreen screen = plugin.getTickerScreenManager().get(uuid);

            if (screen == null) {
                throw new InvalidCommandArgument("Unknown screen provided by the ID of " + uuid);
            }

            return screen;
        });
    }
}
