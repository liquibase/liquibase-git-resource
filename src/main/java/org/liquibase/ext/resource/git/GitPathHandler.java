package org.liquibase.ext.resource.git;

import liquibase.Scope;
import liquibase.resource.*;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.JGitInternalException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryBuilder;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.apache.commons.io.FileUtils;

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

    @Override
    public ResourceAccessor getResourceAccessor(String root) throws IOException {
        //todo ensure exception breaks workflow instead of being severe only
        if (root != null && isGitPathValid(root)) {
            File path = new File(GitConfiguration.GIT_PATH.getCurrentValue());
            File gitPath = new File(path, ".git");
            Boolean cleanup = GitConfiguration.GIT_CLEANUP.getCurrentValue();
            String branch = GitConfiguration.GIT_BRANCH.getCurrentValue();

            if (Boolean.TRUE.equals(cleanup)) {
                this.registerShutdown(path);
            }
            
            if (!path.exists()) {
                path.mkdirs();
            }

            try (Repository repository = new RepositoryBuilder().setGitDir(gitPath).build()) {
                if (repository.getObjectDatabase().exists()) {
                    try (Git git = Git.open(path)) {
                        if (branch != null && !branch.isEmpty()) {
                            git.checkout().setName(branch).call();
                        } else {
                            git.pull().call();
                        }
                        Scope.getCurrentScope().getLog(GitPathHandler.class).fine("Repository updated: " + path);
                    }
                } else {
                    try (Git git = this.getCloneCommand(root, path, branch).call()) {
                        Scope.getCurrentScope().getLog(GitPathHandler.class).fine("Repository cloned: " + path);
                    }
//                    CloneCommand cloneCommand = this.getCloneCommand(root, path, branch);
//                    cloneCommand.call();
//                    Scope.getCurrentScope().getLog(GitPathHandler.class).fine("Repository cloned: " + path);
                }
            } catch (GitAPIException | JGitInternalException e) {
                throw new IOException(e.getMessage());
            }
            Scope.getCurrentScope().getLog(GitPathHandler.class).fine("Return DirectoryResourceAccessor for root path " + path);
            return new DirectoryResourceAccessor(path);
        }
        throw new FileNotFoundException("Unable to locate git repository: " + root);
    }

    @Override
    public Resource getResource(String path) throws IOException {
        return new PathResource(path, Paths.get(path));
    }

    @Override
    public OutputStream createResource(String path) throws IOException {
        return Files.newOutputStream(Paths.get(path), StandardOpenOption.CREATE_NEW);
    }

    private boolean isGitPathValid(String root) {
        // Updated regex to make .git optional
        Pattern pattern = Pattern.compile("((git|ssh|http(s)?)|(git@[\\w\\.]+))(:(//)?)([\\w\\.@\\:/\\-~]+)(\\.git)?(/)?");
        Matcher matcher = pattern.matcher(root);
        return matcher.find();
    }

    private boolean hasGitCredentials(String username, String password) throws IOException {
        if (username == null || password == null) {
            return false;
        }
        if ((username.equals("") && !password.equals("")) || (password.equals("") && !username.equals(""))) {
            throw new IOException("Username and Password are both required for Git Credentials.");
        }
        return true;
    }

    private CloneCommand getCloneCommand(String root, File path, String branch) throws IOException {
        String username = GitConfiguration.GIT_USERNAME.getCurrentValue();
        String password = GitConfiguration.GIT_PASSWORD.getCurrentValue();

        CloneCommand cloneCommand = Git.cloneRepository().setURI(root);
        cloneCommand.setDirectory(path);
        if (this.hasGitCredentials(username, password)) {
            cloneCommand.setCredentialsProvider(new UsernamePasswordCredentialsProvider(username, password));
        }
        if (branch != null && !branch.equals("")) {
            cloneCommand.setBranch(branch);
        }

        return cloneCommand;
    }

    private void registerShutdown(File path) {
        Runtime.getRuntime().addShutdownHook(
            new Thread(
                () -> {
                    try {
                        FileUtils.deleteDirectory(path);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            )
        );
    }
}
