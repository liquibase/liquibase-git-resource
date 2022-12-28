package org.liquibase.ext.resource.git;

import liquibase.Scope;
import liquibase.resource.AbstractPathHandler;
import liquibase.resource.DirectoryResourceAccessor;
import liquibase.resource.Resource;
import liquibase.resource.ResourceAccessor;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

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
        return root.contains(".git");
    }

    @Override
    public ResourceAccessor getResourceAccessor(String root) throws IOException, FileNotFoundException {
        if (root != null) {
            File tmp = new File(".tmp");
            if (!tmp.exists()){
                tmp.mkdirs();
            }
            try {
                Git.cloneRepository().setURI(root).setDirectory(tmp).call();
            } catch (GitAPIException e) {
                throw new IOException("Unable to clone repository: " + root);
            }
            Scope.getCurrentScope().getLog(GitPathHandler.class).fine("Return DirectoryResourceAccessor for root path " + tmp);
            return new DirectoryResourceAccessor(tmp);
        }
        return null;
    }

    @Override
    public Resource getResource(String path) throws IOException {
        return null;
    }

    @Override
    public OutputStream createResource(String path) throws IOException {
        return null;
    }
}
