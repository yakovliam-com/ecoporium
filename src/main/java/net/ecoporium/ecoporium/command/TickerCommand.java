package net.ecoporium.ecoporium.command;

import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import net.ecoporium.ecoporium.EcoporiumPlugin;
import net.ecoporium.ecoporium.api.message.Message;
import net.ecoporium.ecoporium.model.market.Market;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

// TODO test / debug command
// will be removed soon
@CommandAlias("ticker")
public class TickerCommand extends EcoporiumCommand {

    /**
     * Ecoporium command
     *
     * @param manager manager
     * @param plugin  plugin
     */
    public TickerCommand(CommandManager manager, EcoporiumPlugin plugin) {
        super(manager, plugin);
    }

    @Subcommand("getprice")
    @Description("Show the current price for a given stock symbol")
    public void getTickerPrice(Player player, @Single Market market, @Single String symbol) {
        Message.builder()
                .addLine("&7Fetching price for &f" + symbol)
                .build()
                .message(player);

        market.getTicker(symbol).get().thenAccept((stock) -> {
            Message.builder("ticker.price")
                    .addLine("&7Price for &f" + stock.getCurrentStockData() + "&7:")
                    .addLine("&a$" + stock.getCurrentStockData().getQuote().getPrice())
                    .build()
                    .message(player);
        });
    }

    @Default
    @CatchUnknown
    public void onDefault(Player player) {
        Message.builder("ticker.default")
                .addLine("&7Default command")
                .build()
                .message(player);
    }

    @HelpCommand
    public void doHelp(CommandSender sender, CommandHelp help) {
        Message.builder("help")
                .addLine("&eTicker Command")
                .build()
                .message(sender);

        help.showHelp();
    }

    @Override
    protected void registerCompletions() {
    }
}
