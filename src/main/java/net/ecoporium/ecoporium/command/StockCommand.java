package net.ecoporium.ecoporium.command;

import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import net.ecoporium.ecoporium.EcoporiumPlugin;
import net.ecoporium.ecoporium.api.message.Message;
import net.ecoporium.ecoporium.model.market.Market;
import net.ecoporium.ecoporium.model.market.StockTicker;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("stock|ecoporium")
public class StockCommand extends EcoporiumCommand {

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
        ticker.get().thenAccept(stockTicker -> {
            Message.builder()
                    .addLine("&7About &f" + symbol + "&7:")
                    .addLine("&8Previous Close: &7" + stockTicker.getCurrentStockData().getQuote().getPreviousClose())
                    .addLine("&8Price: &7" + stockTicker.getCurrentStockData().getQuote().getPrice())
                    .addLine("&8Open: &7" + stockTicker.getCurrentStockData().getQuote().getOpen())
                    .addLine("&8Average Volume: &7" + stockTicker.getCurrentStockData().getQuote().getAvgVolume())
                    .build();
        });
    }

    @HelpCommand
    public void doHelp(CommandSender sender, CommandHelp help) {
        Message.builder("help")
                .addLine("&eEcoporium Help")
                .build()
                .message(sender);

        help.showHelp();
    }

    @Override
    protected void registerCompletions() {
    }
}
