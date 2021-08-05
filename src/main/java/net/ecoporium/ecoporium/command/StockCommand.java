package net.ecoporium.ecoporium.command;

import co.aikar.commands.BukkitCommandCompletionContext;
import co.aikar.commands.CommandCompletions;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.InvalidCommandArgument;
import co.aikar.commands.annotation.*;
import net.ecoporium.ecoporium.EcoporiumPlugin;
import net.ecoporium.ecoporium.api.message.Message;
import net.ecoporium.ecoporium.model.market.Market;
import net.ecoporium.ecoporium.model.market.StockTicker;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.stream.Collectors;

@CommandAlias("stock")
public class StockCommand extends AbstractEcoporiumCommand {

    /**
     * Ecoporium command
     *
     * @param manager manager
     * @param plugin  plugin
     */
    public StockCommand(CommandManager manager, EcoporiumPlugin plugin) {
        super(manager, plugin);
    }

    @Subcommand("info")
    @Description("Retrieves information about a specific stock")
    public void onStockInfo(Player player, @Single Market market, @Single String symbol) {
        Message.builder()
                .addLine("&7Fetching information on " + symbol)
                .build()
                .message(player);

        StockTicker ticker = market.getTicker(symbol);

        // if null, couldn't find in that market
        if (ticker == null) {
            Message.builder()
                    .addLine("&cOops! We couldn't find any information on &f" + symbol + " &cin the &f" + market.getHandle() + " &cmarket.")
                    .build()
                    .message(player);
            return;
        }

        // get / update
        ticker.fetchStockData().thenAccept((v) -> Message.builder()
                .addLine("&7About &f" + symbol + "&7:")
                .addLine("&8Previous Close: &7" + ticker.getCurrentStockData().getQuote().getPreviousClose())
                .addLine("&8Price: &7" + ticker.getCurrentStockData().getQuote().getPrice())
                .addLine("&8Open: &7" + ticker.getCurrentStockData().getQuote().getOpen())
                .addLine("&8Average Volume: &7" + ticker.getCurrentStockData().getQuote().getAvgVolume())
                .build());
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
