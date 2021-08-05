package net.ecoporium.ecoporium.message;

import net.ecoporium.ecoporium.api.message.Message;

public class Messages {

    /**
     * Ecoporium command
     */

    // create static whitelist
    public Message ecoporiumCreateStaticWhitelist = Message.builder()
            .addLine("&cOops! That symbol is not include in that market's whitelist.")
            .build();

    // create static success
    public Message ecoporiumCreateStaticWaiting = Message.builder()
            .addLine("&7Right click on a block to create a ticker for &f%symbol%&7.")
            .build();

    // cancel not in placement session
    public Message ecoporiumCreateCancelNotInPlacementSession = Message.builder()
            .addLine("&cYou're not in a placement session!")
            .build();

    // canceled
    public Message ecoporiumCreateCanceled = Message.builder()
            .addLine("&7Canceled.")
            .build();

    /**
     * Delete
     */

    // deleted
    public Message ecoporiumDeleteDeleted = Message.builder()
            .addLine("&7Deleted.")
            .build();

    // delete can't find
    public Message ecoporiumDeleteCantFind = Message.builder()
            .addLine("&cWe can't find the screen. Are you looking at one?")
            .build();

    /**
     * Placement
     */

    // start placing
    public Message ecoporiumStartPlacing = Message.builder()
            .addLine("&7Start placing a &f%widthmaps%&7x&f%heightmaps% &7sized screen, going from &ftop to bottom &7& &fleft to right&7.")
            .build();
}
