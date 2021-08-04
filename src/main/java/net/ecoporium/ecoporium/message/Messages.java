package net.ecoporium.ecoporium.message;

import net.ecoporium.ecoporium.api.message.Message;

public class Messages {

    public Message ecoporiumCreateStaticWhitelist = Message.builder()
            .addLine("&cOops! That symbol is not include in that market's whitelist.")
            .build();

    public Message ecoporiumCreateNotInSession = Message.builder()
            .addLine("&cYou're not currently in a creation session.")
            .build();

    public Message ecoporiumCreateSessionIncomplete = Message.builder()
            .addLine("&cYour session is incomplete!")
            .build();

    public Message ecoporiumCreateSessionError = Message.builder()
            .addLine("&cSomething went wrong...")
            .build();

    public Message ecoporiumCreateStaticSuccess = Message.builder()
            .addLine("&7You created a screen with the live data from &f%symbol%&7.")
            .build();
}
