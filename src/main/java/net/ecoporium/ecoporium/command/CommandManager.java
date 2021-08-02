package net.ecoporium.ecoporium.command;

import co.aikar.commands.BukkitCommandManager;
import co.aikar.commands.InvalidCommandArgument;
import co.aikar.commands.MessageType;
import co.aikar.commands.MinecraftMessageKeys;
import net.ecoporium.ecoporium.EcoporiumPlugin;
import net.ecoporium.ecoporium.model.market.Market;
import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.Collections;

public class CommandManager extends BukkitCommandManager {

    public CommandManager(EcoporiumPlugin plugin) {
        super(plugin);

        enableUnstableAPI("help");
        enableUnstableAPI("brigadier");

        setFormat(MessageType.INFO, ChatColor.WHITE);
        setFormat(MessageType.HELP, ChatColor.GRAY);
        setFormat(MessageType.ERROR, ChatColor.RED);
        setFormat(MessageType.SYNTAX, ChatColor.GRAY);

        getCommandContexts().registerIssuerAwareContext(Market.class, c -> {
            String firstArg = c.getFirstArg();

            Market market = plugin.getMarketCache().get(firstArg, null);

            if (market != null) {
                c.popFirstArg();
            } else {
                throw new InvalidCommandArgument("Invalid market name provided");
            }

            return market;
        });

        // TODO add more commands here, registering them
        Arrays.asList(
                new TickerCommand(this, plugin),
                new ChartCommand(this, plugin)
        ).forEach(c -> {
            c.registerCompletions();
            this.registerCommand(c);
        });
    }
}
