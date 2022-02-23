package org.liquibase.ext.resource.git;

import liquibase.configuration.AutoloadedConfigurations;
import liquibase.configuration.ConfigurationDefinition;

public class GitResourceConfiguration implements AutoloadedConfigurations {

    public static ConfigurationDefinition<String> REMOTE_RESOURCE_PATH;

    static {
        ConfigurationDefinition.Builder builder = new ConfigurationDefinition.Builder("remote");
        REMOTE_RESOURCE_PATH = builder.define("resourcePath", String.class)
                .setDescription("Path to Remote Resources")
                .build();
    }
}
