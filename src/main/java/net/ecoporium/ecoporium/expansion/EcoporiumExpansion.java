package net.ecoporium.ecoporium.expansion;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.ecoporium.ecoporium.EcoporiumPlugin;
import net.ecoporium.ecoporium.market.Market;
import net.ecoporium.ecoporium.market.stock.StockTicker;
import net.ecoporium.ecoporium.user.EcoporiumUser;
import net.ecoporium.ecoporium.util.NumberUtil;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class EcoporiumExpansion extends PlaceholderExpansion {

    /**
     * Ecoporium plugin
     */
    private final EcoporiumPlugin plugin;

    /**
     * Ecoporium expansion
     *
     * @param plugin plugin
     */
    public EcoporiumExpansion(EcoporiumPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getAuthor() {
        return plugin.getDescription().getAuthors().stream()
                .findFirst()
                .orElse("yakovliam");
    }

    @Override
    public @NotNull String getIdentifier() {
        return "ecoporium";
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true; // This is required or else PlaceholderAPI will unregister the Expansion on reload
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        String[] parts = params.split("_");
        String request = parts[0];

        if (request.equalsIgnoreCase("price")) {
            if (parts.length != 3) {
                return null;
            }
            String marketHandle = parts[1];
            String symbol = parts[2];

            Market<?> market = plugin.getMarketCache().getCache().get(marketHandle).join();

            if (market == null) {
                return null;
            }

            if (!market.containsStock(symbol)) {
                return null;
            }

            StockTicker<?> ticker = market.getStock(symbol);

            return NumberUtil.formatToPlaces(ticker.getCurrentQuote().getPrice(), 2);
        }

        if(request.equalsIgnoreCase("direction") || request.equalsIgnoreCase("historical-analysis")){
            if (parts.length != 3) {
                return null;
            }

            String marketHandle = parts[1];
            String symbol = parts[2];

            Market<?> market = plugin.getMarketCache().getCache().get(marketHandle).join();

            if (market == null) {
                return null;
            }

            if (!market.containsStock(symbol)) {
                return null;
            }

            StockTicker<?> ticker = market.getStock(symbol);

            return ticker.getHistoricalAnalysis().name();
        }

        if (request.equalsIgnoreCase("player-shares")) {
            if (parts.length != 3) {
                return null;
            }
            String marketHandle = parts[1];
            String symbol = parts[2];

            EcoporiumUser user = plugin.getUserCache().getCache().get(player.getUniqueId()).join();

            return Integer.toString(user.getShares(marketHandle, symbol));
        }

        return null;
    }
}