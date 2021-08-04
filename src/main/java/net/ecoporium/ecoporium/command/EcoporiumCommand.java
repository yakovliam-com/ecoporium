package net.ecoporium.ecoporium.command;

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
import net.ecoporium.ecoporium.model.market.Market;
import net.ecoporium.ecoporium.placement.PlacementData;
import net.ecoporium.ecoporium.ticker.StaticTickerScreen;
import net.ecoporium.ecoporium.ticker.info.ScreenInfo;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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

    @Subcommand("create")
    @CommandPermission("ecoporium.command.create")
    public class CreateCommand {

        @Subcommand("static")
        @CommandPermission("ecoporium.command.create.screen.static")
        public void onCreateStatic(Player player, @Single Market market, @Single String symbol) {
            // if market doesn't have in whitelist
            if (market.getWhitelistOptions().getTickers().size() >= 1 && !market.getWhitelistOptions().getTickers().contains(symbol)) {
                plugin.getMessages().ecoporiumCreateStaticWhitelist.message(player);
                return;
            }

            // add to waiting list
            plugin.getPlacementHandler().add(player.getUniqueId(), new PlacementData(new ScreenInfo(400, 300), market, symbol));

            plugin.getMessages().ecoporiumCreateStaticWaiting.message(player, "%symbol%", symbol);
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
                return null;
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
    }
}
