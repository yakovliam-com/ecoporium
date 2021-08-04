package net.ecoporium.ecoporium.command;

import co.aikar.commands.BukkitCommandCompletionContext;
import co.aikar.commands.CommandCompletions;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.InvalidCommandArgument;
import co.aikar.commands.annotation.*;
import net.ecoporium.ecoporium.EcoporiumPlugin;
import net.ecoporium.ecoporium.api.message.Message;
import net.ecoporium.ecoporium.model.market.Market;
import net.ecoporium.ecoporium.session.screen.TickerScreenCreatorSession;
import net.ecoporium.ecoporium.ticker.StaticTickerScreen;
import net.ecoporium.ecoporium.util.ScreenPositionUtil;
import net.ecoporium.ecoporium.util.WandItemUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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

    @Subcommand("wand")
    @CommandPermission("ecoporium.command.wand")
    public void onWand(Player player) {
        // give wand item
        ItemStack wandItem = WandItemUtil.createWandItem();
        player.getInventory().addItem(wandItem);
        player.updateInventory();
    }

    @Subcommand("create static")
    public void onCreateStatic(Player player, @Single Market market, @Single String symbol) {
        // if market doesn't have in whitelist
        if (market.getWhitelistOptions().getTickers().size() >= 1 && !market.getWhitelistOptions().getTickers().contains(symbol)) {
            plugin.getMessages().ecoporiumCreateStaticWhitelist.message(player);
            return;
        }

        // if not in session
        if (!plugin.getTickerScreenCreatorSessionManager().isInSession(player.getUniqueId())) {
            plugin.getMessages().ecoporiumCreateNotInSession.message(player);
            return;
        }

        // get session
        TickerScreenCreatorSession screenCreatorSession = plugin.getTickerScreenCreatorSessionManager().getSession(player.getUniqueId());

        // if incomplete
        if (!screenCreatorSession.isComplete()) {
            plugin.getMessages().ecoporiumCreateSessionIncomplete.message(player);
            return;
        }
        // if the screen isn't a valid shape
        if (ScreenPositionUtil.calculateNumberOfMaps(screenCreatorSession.getScreenPositionalInfo()) == null) {
            plugin.getMessages().ecoporiumCreateSessionIncomplete.message(player);
            return;
        }

        // create
        StaticTickerScreen screen = plugin.getTickerScreenCreatorSessionManager().createStaticTickerScreen(player.getUniqueId(), symbol);
        // TODO save to storage

        plugin.getMessages().ecoporiumCreateStaticSuccess.message(player, "%symbol%", symbol);
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
