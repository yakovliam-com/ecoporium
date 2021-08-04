package net.ecoporium.ecoporium.command;

import co.aikar.commands.BukkitCommandManager;
import co.aikar.commands.MessageType;
import net.ecoporium.ecoporium.EcoporiumPlugin;
import org.bukkit.ChatColor;

import java.util.Arrays;

public class CommandManager extends BukkitCommandManager {

    public CommandManager(EcoporiumPlugin plugin) {
        super(plugin);

        enableUnstableAPI("help");
        enableUnstableAPI("brigadier");

        setFormat(MessageType.INFO, ChatColor.WHITE);
        setFormat(MessageType.HELP, ChatColor.GRAY);
        setFormat(MessageType.ERROR, ChatColor.RED);
        setFormat(MessageType.SYNTAX, ChatColor.GRAY);

        // TODO add more commands here, registering them
        Arrays.asList(
                new EcoporiumCommand(this, plugin),
                new StockCommand(this, plugin)
        ).forEach(c -> {
            c.registerCompletions();
            c.registerContexts();

            this.registerCommand(c);
        });
    }
}
