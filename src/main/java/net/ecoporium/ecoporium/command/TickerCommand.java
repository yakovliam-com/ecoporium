package net.ecoporium.ecoporium.command;

import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import net.ecoporium.ecoporium.EcoporiumPlugin;
import net.ecoporium.ecoporium.api.message.Message;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("ticker")
public class TickerCommand extends EcoporiumCommand {

    /**
     * Ecoporium command
     *
     * @param plugin plugin
     */
    public TickerCommand(EcoporiumPlugin plugin) {
        super(plugin);
    }

    @Subcommand("getprice")
    @Description("Show the current price for a given stock symbol")
    public void getTickerPrice(Player player, @Single String symbol) {
        Message.builder("ticker.fetching")
                .addLine("&7Fetching price for &f" + symbol)
                .build()
                .message(player);

        getPlugin().getMarketCache().get("nasdaq", null).getTicker(symbol).thenAccept((stock) -> {
            System.out.println("stock.getQuote().getPrice() = " + stock.getQuote().getPrice());

            Message.builder("ticker.price")
                    .addLine("&7Price for &f" + stock.getName() + "&7:")
                    .addLine("&a$" + stock.getQuote().getPrice())
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
}
