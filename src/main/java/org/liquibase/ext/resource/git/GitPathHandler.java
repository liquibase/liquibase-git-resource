package org.liquibase.ext.resource.git;

import liquibase.Scope;
import liquibase.resource.*;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GitPathHandler extends AbstractPathHandler {

    @Override
    public final int getPriority(String root) {
        Scope.getCurrentScope().getLog(GitPathHandler.class).fine("Check GitPathHandler for root path " + root);
        if (root == null) {
            return PRIORITY_NOT_APPLICABLE;
        }
        if (!isGitPathValid(root)) {
            return PRIORITY_NOT_APPLICABLE;
        }
        Scope.getCurrentScope().getLog(GitPathHandler.class).fine("GitPathHandler supports root path " + root);
        return PRIORITY_SPECIALIZED;
    }

    private boolean isGitPathValid(String root) {
        // https://www.debuggex.com/r/H4kRw1G0YPyBFjfm
        Pattern pattern = Pattern.compile("((git|ssh|http(s)?)|(git@[\\w\\.]+))(:(//)?)([\\w\\.@\\:/\\-~]+)(\\.git)(/)?");
        Matcher matcher = pattern.matcher(root);
        return matcher.find();
    }

    @Override
    public ResourceAccessor getResourceAccessor(String root) throws IOException, FileNotFoundException {
        if (root != null) {
            File tmp = new File(".tmp");
            if (!tmp.exists()){
                tmp.mkdirs();
                try {
                    Git.cloneRepository().setURI(root).setDirectory(tmp).call();
                } catch (GitAPIException e) {
                    throw new IOException("Unable to clone repository: " + root);
                }
            } else {
                Git.open(tmp).pull();
            }
            Scope.getCurrentScope().getLog(GitPathHandler.class).fine("Return DirectoryResourceAccessor for root path " + tmp);
            return new DirectoryResourceAccessor(tmp);
        }
        throw new FileNotFoundException("Unable to locate temp directory for git repository");
    }

    @Override
    public Resource getResource(String path) throws IOException {
        return new PathResource(path, Paths.get(path));
    }

    @Override
    public OutputStream createResource(String path) throws IOException {
        return Files.newOutputStream(Paths.get(path), StandardOpenOption.CREATE_NEW);
    }
}
