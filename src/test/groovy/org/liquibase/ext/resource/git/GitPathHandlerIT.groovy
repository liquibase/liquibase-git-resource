package org.liquibase.ext.resource.git

import liquibase.resource.DirectoryResourceAccessor
import org.apache.commons.io.FileUtils
import spock.lang.Requires
import spock.lang.Specification

class GitPathHandlerIT extends Specification {

    def gitPathHandler = new GitPathHandler()

    def cleanup() {
        FileUtils.deleteDirectory(new File(".tmp"));
    }

    def "Can Clone Public Repository"() {
        when:
        def resource = gitPathHandler.getResourceAccessor("https://github.com/liquibase/liquibase-github-action-example.git")

        then:
        resource instanceof DirectoryResourceAccessor
    }


    @Requires({ env["LIQUIBASE_GIT_USERNAME"] })
    def "Can Clone Private Repository"() {
        when:
        def resource = gitPathHandler.getResourceAccessor("https://github.com/liquibase/hashicorp-vault-plugin.git")

        then:
        resource instanceof DirectoryResourceAccessor
    }
}
