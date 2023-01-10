package org.liquibase.ext.resource.git

import liquibase.plugin.Plugin
import liquibase.resource.DirectoryResourceAccessor
import org.apache.commons.io.FileUtils
import spock.lang.Specification
import spock.lang.Unroll

class GitPathHandlerIT extends Specification {

    def cleanup() {
        FileUtils.deleteDirectory(new File(".tmp"));
    }

    def "Can Clone Public Repository"() {
        given:
        def gitPathHandler = new GitPathHandler()

        when:
        def resource = gitPathHandler.getResourceAccessor("https://github.com/liquibase/liquibase-github-action-example.git")

        then:
        resource instanceof DirectoryResourceAccessor
    }


    def "Can Clone Private Repository"() {
        given:
        def gitPathHandler = new GitPathHandler()

        when:
        def resource = gitPathHandler.getResourceAccessor("https://github.com/liquibase/hashicorp-vault-plugin.git")

        then:
        resource instanceof DirectoryResourceAccessor
    }
}
