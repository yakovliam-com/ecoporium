package net.ecoporium.ecoporium.message;

import net.ecoporium.ecoporium.api.message.Message;

public class Messages {

    /**
     * Ecoporium command
     */

    public Message help = Message.builder()
            .addLine("&7Ecoporium Help")
            .build();

    // market nonexistent
    public Message marketNonexistent = Message.builder()
            .addLine("&7That market does not exist.")
            .build();

    // market exists already
    public Message marketExistsAlready = Message.builder()
            .addLine("&7A market with that name already exists.")
            .build();

    // something went wrong
    public Message somethingWentWrong = Message.builder()
            .addLine("&cSomething went wrong!")
            .build();
}
