package org.liquibase.ext.resource.git;

import liquibase.configuration.AutoloadedConfigurations;
import liquibase.configuration.ConfigurationDefinition;

public class GitConfiguration implements AutoloadedConfigurations {

    public static final ConfigurationDefinition<String> GIT_USERNAME;
    public static final ConfigurationDefinition<String> GIT_PASSWORD;
    public static final ConfigurationDefinition<String> GIT_PATH;
    public static final ConfigurationDefinition<String> GIT_BRANCH;
    public static final ConfigurationDefinition<Boolean> GIT_CLEANUP;

    static {
        ConfigurationDefinition.Builder builder = new ConfigurationDefinition.Builder("liquibase.git");
        GIT_USERNAME = builder.define("username", String.class)
                .addAliasKey("git.username")
                .setDescription("Git username for searchPath repository URL")
                .build();
        GIT_PASSWORD = builder.define("password", String.class)
                .addAliasKey("git.password")
                .setDescription("Git password for searchPath repository URL")
                .build();
        GIT_PATH = builder.define("path", String.class)
                .addAliasKey("git.path")
                .setDescription("Path in which to clone repository")
                .setDefaultValue(".tmp")
                .build();
        GIT_BRANCH = builder.define("branch", String.class)
                .addAliasKey("git.branch")
                .setDescription("Git branch to clone into git.path")
                .build();
        GIT_CLEANUP = builder.define("cleanup", Boolean.class)
                .addAliasKey("git.cleanup")
                .setDescription("Remove local repository path after run")
                .setDefaultValue(true)
                .build();
    }


}
