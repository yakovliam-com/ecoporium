package net.ecoporium.ecoporium.message;

import net.ecoporium.ecoporium.api.message.Message;

public class Messages {

    public Message ecoporiumCreateStaticWhitelist = Message.builder()
            .addLine("&cOops! That symbol is not include in that market's whitelist.")
            .build();

    public Message getEcoporiumCreateNotInSession = Message.builder()
            .addLine("&cYou're not currently in a creation session.")
            .build();

}
