package net.ecoporium.ecoporium.command;

import co.aikar.commands.BaseCommand;
import net.ecoporium.ecoporium.EcoporiumPlugin;

public abstract class EcoporiumCommand extends BaseCommand {

    /**
     * Plugin
     */
    private final EcoporiumPlugin plugin;

    /**
     * Ecoporium command
     *
     * @param plugin plugin
     */
    public EcoporiumCommand(EcoporiumPlugin plugin) {
        this.plugin = plugin;
    }

    protected EcoporiumPlugin getPlugin() {
        return plugin;
    }
}
