package com.yakovliam.ecoporium.expansion;

import com.yakovliam.ecoporium.EcoporiumPlugin;
import com.yakovliam.ecoporium.api.market.Market;
import com.yakovliam.ecoporium.api.market.stock.StockTicker;
import com.yakovliam.ecoporium.api.user.EcoporiumUser;
import com.yakovliam.ecoporium.util.NumberUtil;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
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

            Market<?> market = plugin.marketCache().getCache().get(marketHandle).join();

            if (market == null) {
                return null;
            }

            if (!market.containsStock(symbol)) {
                return null;
            }

            StockTicker<?> ticker = market.getStock(symbol);

            if (ticker.getCurrentQuote().isEmpty()) {
                return "unavailable";
            }

            return NumberUtil.formatToPlaces(ticker.getCurrentQuote().get().getPrice(), 2);
        }

        if (request.equalsIgnoreCase("direction") || request.equalsIgnoreCase("historical-analysis")) {
            if (parts.length != 3) {
                return null;
            }

            String marketHandle = parts[1];
            String symbol = parts[2];

            Market<?> market = plugin.marketCache().getCache().get(marketHandle).join();

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

            EcoporiumUser user = plugin.userCache().getCache().get(player.getUniqueId()).join();

            return Integer.toString(user.numberOfShares(marketHandle, symbol));
        }

        return null;
    }
}