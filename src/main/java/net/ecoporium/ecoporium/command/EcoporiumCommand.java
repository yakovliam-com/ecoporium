package net.ecoporium.ecoporium.command;

import co.aikar.commands.BaseCommand;
import net.ecoporium.ecoporium.EcoporiumPlugin;

public abstract class EcoporiumCommand extends BaseCommand {

    /**
     * Plugin
     */
    protected final EcoporiumPlugin plugin;

    /**
     * Manager
     */
    protected final CommandManager manager;

    /**
     * Ecoporium command
     *
     * @param manager manager
     * @param plugin  plugin
     */
    public EcoporiumCommand(CommandManager manager, EcoporiumPlugin plugin) {
        this.plugin = plugin;
        this.manager = manager;
    }

    protected abstract void registerCompletions();
}
