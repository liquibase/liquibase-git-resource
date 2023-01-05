package org.liquibase.ext.resource.git

import liquibase.plugin.Plugin
import liquibase.resource.DirectoryResourceAccessor
import spock.lang.Specification
import spock.lang.Unroll
import org.apache.commons.io.FileUtils;

class GitPathHandlerTest extends Specification {

    def cleanup() {
        FileUtils.deleteDirectory(new File(".tmp"));
    }

    @Unroll
    def supports() {
        expect:
        new GitPathHandler().getPriority(input) == expected

        where:
        input                                           | expected
        "git@github.com:user/project.git"               | Plugin.PRIORITY_SPECIALIZED
        "https://github.com/user/project.git"           | Plugin.PRIORITY_SPECIALIZED
        "http://github.com/user/project.git"            | Plugin.PRIORITY_SPECIALIZED
        "git@192.168.101.127:user/project.git"          | Plugin.PRIORITY_SPECIALIZED
        "https://192.168.101.127/user/project.git"      | Plugin.PRIORITY_SPECIALIZED
        "http://192.168.101.127/user/project.git"       | Plugin.PRIORITY_SPECIALIZED
        "ssh://user@host.xz:port/path/to/repo.git/"     | Plugin.PRIORITY_SPECIALIZED
        "ssh://user@host.xz/path/to/repo.git/"          | Plugin.PRIORITY_SPECIALIZED
        "ssh://host.xz:port/path/to/repo.git/"          | Plugin.PRIORITY_SPECIALIZED
        "ssh://host.xz/path/to/repo.git/"               | Plugin.PRIORITY_SPECIALIZED
        "ssh://user@host.xz/path/to/repo.git/"          | Plugin.PRIORITY_SPECIALIZED
        "ssh://host.xz/path/to/repo.git/"               | Plugin.PRIORITY_SPECIALIZED
        "ssh://user@host.xz/~user/path/to/repo.git/"    | Plugin.PRIORITY_SPECIALIZED
        "ssh://host.xz/~user/path/to/repo.git/"         | Plugin.PRIORITY_SPECIALIZED
        "ssh://user@host.xz/~/path/to/repo.git"         | Plugin.PRIORITY_SPECIALIZED
        "ssh://host.xz/~/path/to/repo.git"              | Plugin.PRIORITY_SPECIALIZED
        "git://host.xz/path/to/repo.git/"               | Plugin.PRIORITY_SPECIALIZED
        "git://host.xz/~user/path/to/repo.git/"         | Plugin.PRIORITY_SPECIALIZED
        "http://host.xz/path/to/repo.git/"              | Plugin.PRIORITY_SPECIALIZED
        "https://host.xz/path/to/repo.git/"             | Plugin.PRIORITY_SPECIALIZED
        "/path/to/repo.git/"                            | Plugin.PRIORITY_NOT_APPLICABLE
        "path/to/repo.git/"                             | Plugin.PRIORITY_NOT_APPLICABLE
        "~/path/to/repo.git"                            | Plugin.PRIORITY_NOT_APPLICABLE
        "file:///path/to/repo.git/"                     | Plugin.PRIORITY_NOT_APPLICABLE
        "file://~/path/to/repo.git/"                    | Plugin.PRIORITY_NOT_APPLICABLE
        "user@host.xz:/path/to/repo.git/"               | Plugin.PRIORITY_NOT_APPLICABLE
        "host.xz:/path/to/repo.git/"                    | Plugin.PRIORITY_NOT_APPLICABLE
        "user@host.xz:~user/path/to/repo.git/"          | Plugin.PRIORITY_NOT_APPLICABLE
        "host.xz:~user/path/to/repo.git/"               | Plugin.PRIORITY_NOT_APPLICABLE
        "user@host.xz:path/to/repo.git"                 | Plugin.PRIORITY_NOT_APPLICABLE
        "host.xz:path/to/repo.git"                      | Plugin.PRIORITY_NOT_APPLICABLE
        "rsync://host.xz/path/to/repo.git/"             | Plugin.PRIORITY_NOT_APPLICABLE
    }

    def "Can Not Clone Repository"() {
        given:
            def gitPathHandler = new GitPathHandler()

        when:
            gitPathHandler.getResourceAccessor("ssh://host.xz/path/to/repo.git/")

        then:
            thrown IOException
    }

    def "Can Not Locate Git Repository"() {
        given:
        def gitPathHandler = new GitPathHandler()

        when:
        gitPathHandler.getResourceAccessor("host.xz:path/to/repo.git")

        then:
        thrown FileNotFoundException
    }
}
