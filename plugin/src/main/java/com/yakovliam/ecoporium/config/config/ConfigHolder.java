package com.yakovliam.ecoporium.config.config;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
@SuppressWarnings("FieldMayBeFinal")
public class ConfigHolder implements com.yakovliam.ecoporium.api.config.ConfigHolder {

    private String[] authors = new String[]{"yakovliam"};
}
