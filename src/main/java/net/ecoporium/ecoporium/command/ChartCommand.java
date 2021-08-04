package net.ecoporium.ecoporium.command;

import co.aikar.commands.BukkitCommandCompletionContext;
import co.aikar.commands.CommandCompletions;
import co.aikar.commands.InvalidCommandArgument;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Single;
import com.github.johnnyjayjay.spigotmaps.RenderedMap;
import com.github.johnnyjayjay.spigotmaps.rendering.ImageRenderer;
import com.github.johnnyjayjay.spigotmaps.util.ImageTools;
import net.ecoporium.ecoporium.EcoporiumPlugin;
import net.ecoporium.ecoporium.api.message.Message;
import net.ecoporium.ecoporium.model.market.Market;
import net.ecoporium.ecoporium.ticker.StaticTickerScreen;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@CommandAlias("chart")
public class ChartCommand extends AbstractEcoporiumCommand {


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

    @Default
    public void onChart(Player player, @Single Market market, @Single String symbol) {
        Message.builder()
                .addLine("&7Rendering live chart for &f" + symbol + " &7in the &f" + market.getHandle() + " &7market.")
                .build()
                .message(player);

        // calculate number of maps
        int numOfRenderers = 25; // hardcoded for now

        List<ImageRenderer> renderers = new ArrayList<>();

        // create renderers
        for (int i = 0; i < numOfRenderers; i++) {
            renderers.add(ImageRenderer.builder()
                    .image(ImageTools.createSingleColoredImage(Color.CYAN))
                    .renderOnce(false)
                    .build());
        }

        List<RenderedMap> renderedMaps = renderers.stream()
                .map(RenderedMap::create)
                .collect(Collectors.toList());

        int counter = 1;
        List<ItemStack> maps = new ArrayList<>();

        for (RenderedMap renderedMap : renderedMaps) {
            maps.add(renderedMap.createItemStack("", Integer.toString(counter)));
            counter++;
        }

        // give to player
        player.getInventory().addItem(maps.toArray(ItemStack[]::new));
        player.updateInventory();

        // TODO ..?
    }
}
