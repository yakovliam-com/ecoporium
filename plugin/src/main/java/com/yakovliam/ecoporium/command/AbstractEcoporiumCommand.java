package com.yakovliam.ecoporium.command;

import co.aikar.commands.BaseCommand;
import com.yakovliam.ecoporium.EcoporiumPlugin;

public abstract class AbstractEcoporiumCommand extends BaseCommand {

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
    public AbstractEcoporiumCommand(CommandManager manager, EcoporiumPlugin plugin) {
        this.plugin = plugin;
        this.manager = manager;
    }

    protected abstract void registerContexts();
}
