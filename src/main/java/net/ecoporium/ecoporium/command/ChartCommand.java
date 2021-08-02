package net.ecoporium.ecoporium.command;

import co.aikar.commands.BukkitCommandCompletionContext;
import co.aikar.commands.CommandCompletions;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Single;
import net.ecoporium.ecoporium.EcoporiumPlugin;
import net.ecoporium.ecoporium.api.message.Message;
import net.ecoporium.ecoporium.model.market.Market;
import org.bukkit.entity.Player;

import java.util.stream.Collectors;

@CommandAlias("chart")
public class ChartCommand extends EcoporiumCommand {


    /**
     * Ecoporium command
     *
     * @param manager manager
     * @param plugin  plugin
     */
    public ChartCommand(CommandManager manager, EcoporiumPlugin plugin) {
        super(manager, plugin);
    }

    @Override
    protected void registerCompletions() {
        CommandCompletions<BukkitCommandCompletionContext> commandCompletions = manager.getCommandCompletions();

        commandCompletions.registerAsyncCompletion("market", c -> plugin.getMarketCache().getMap().values().stream()
                .map(Market::getHandle)
                .collect(Collectors.toList()));
    }

    @Default
    public void onChart(Player player, @Single Market market, @Single String symbol) {
        Message.builder()
                .addLine("&7Rendering live chart for &f" + symbol + " &7in the &f" + market.getHandle() + " &7market.")
                .build()
                .message(player);

    }
}
