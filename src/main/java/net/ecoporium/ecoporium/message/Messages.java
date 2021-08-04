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

    /**
     * Placement listener
     */

    // canceled
    public Message ecoporiumPlacementCanceled = Message.builder()
            .addLine("&7Canceled.")
            .build();

    // invalid facing
    public Message ecoporiumPlacementInvalidFacing = Message.builder()
            .addLine("&cInvalid block facing!")
            .build();

    // invalid direction
    public Message ecoporiumPlacementInvalidDirection = Message.builder()
            .addLine("&cInvalid direction - there was a problem trying to calculate and do math and stuff.")
            .build();

    // insufficient wall
    public Message ecoporiumPlacementInsufficientWall = Message.builder()
            .addLine("&cInsufficient wall/space to place the screen!")
            .build();

    // overlapping entity
    public Message ecoporiumPlacementOverlappingEntity = Message.builder()
            .addLine("&cThere's an overlapping entity!")
            .build();

    // success
    public Message ecoporiumPlacementSuccess = Message.builder()
            .addLine("&aSuccess!")
            .build();
}
