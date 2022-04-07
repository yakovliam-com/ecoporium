package com.yakovliam.ecoporium.listener;

import com.yakovliam.ecoporium.EcoporiumPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public record PlayerListener(EcoporiumPlugin plugin) implements Listener {

    /**
     * Player listener
     *
     * @param plugin plugin
     */
    public PlayerListener {
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // add to cache
        plugin.userCache().getCache().get(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        // invalidate from cache
        plugin.userCache().getCache().synchronous().invalidate(event.getPlayer().getUniqueId());
    }
}
