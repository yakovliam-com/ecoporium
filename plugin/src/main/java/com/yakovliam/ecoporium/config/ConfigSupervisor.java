package com.yakovliam.ecoporium.config;

import com.yakovliam.ecoporium.EcoporiumPlugin;
import com.yakovliam.ecoporium.config.config.ConfigHolder;
import com.yakovliam.ecoporium.config.messages.MessagesHolder;

public class ConfigSupervisor {

    /**
     * Config factory
     */
    private final ConfigFactory configFactory;

    private MessagesHolder messagesHolder;

    private ConfigHolder configHolder;

    /**
     * Config supervisor
     *
     * @param plugin plugin
     */
    public ConfigSupervisor(EcoporiumPlugin plugin) {
        this.configFactory = new ConfigFactory(plugin);

        reload();

        messages();
        config();
    }

    public void reload() {
        this.messagesHolder = null;
        this.configHolder = null;
    }

    public MessagesHolder messages() {
        if (this.messagesHolder == null) {
            this.messagesHolder = this.configFactory.messages();
        }

        return this.messagesHolder;
    }

    public ConfigHolder config() {
        if (this.configHolder == null) {
            this.configHolder = this.configFactory.config();
        }

        return this.configHolder;
    }
}
