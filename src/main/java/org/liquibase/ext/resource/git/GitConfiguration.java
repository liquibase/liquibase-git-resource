package org.liquibase.ext.resource.git;

import liquibase.configuration.AutoloadedConfigurations;
import liquibase.configuration.ConfigurationDefinition;
import liquibase.configuration.ConfigurationValueObfuscator;

public class GitConfiguration implements AutoloadedConfigurations {

    public static final ConfigurationDefinition<String> GIT_USERNAME;
    public static final ConfigurationDefinition<String> GIT_PASSWORD;
    public static final ConfigurationDefinition<String> GIT_PATH;
    public static final ConfigurationDefinition<String> GIT_BRANCH;
    public static final ConfigurationDefinition<Boolean> GIT_CLEANUP;
    public static final ConfigurationDefinition<Integer> GIT_FETCH_DEPTH;

    static {
        ConfigurationDefinition.Builder builder = new ConfigurationDefinition.Builder("liquibase.git");
        GIT_USERNAME = builder.define("username", String.class)
                .addAliasKey("git.username")
                .setDescription("Git username for searchPath repository URL")
                .build();
        GIT_PASSWORD = builder.define("password", String.class)
                .addAliasKey("git.password")
                .setDescription("Git password for searchPath repository URL")
                .setValueObfuscator(ConfigurationValueObfuscator.STANDARD)
                .build();
        GIT_PATH = builder.define("path", String.class)
                .addAliasKey("git.path")
                .setDescription("Path in which to clone repository")
                .setDefaultValue(".tmp")
                .build();
        GIT_BRANCH = builder.define("branch", String.class)
                .addAliasKey("git.branch")
                .setDescription("Git branch to clone into git.path. Can be specified as ref name (refs/heads/main), branch name (main) or tag name (v1.2.3)")
                .build();
        GIT_CLEANUP = builder.define("cleanup", Boolean.class)
                .addAliasKey("git.cleanup")
                .setDescription("Remove local repository path after run")
                .setDefaultValue(true)
                .build();
        GIT_FETCH_DEPTH = builder.define("fetch_depth", Integer.class)
                .addAliasKey("git.fetch_depth")
                .setDescription("Creates a shallow clone with a history truncated to the specified number of commits")
                .build();
    }


}
