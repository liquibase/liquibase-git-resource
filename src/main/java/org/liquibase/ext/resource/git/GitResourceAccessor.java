package org.liquibase.ext.resource.git;

import liquibase.resource.AbstractResourceAccessor;
import liquibase.resource.InputStreamList;

import java.io.IOException;
import java.util.SortedSet;

public class GitResourceAccessor extends AbstractResourceAccessor {
    @Override
    public InputStreamList openStreams(String s, String s1) throws IOException {
        return null;
    }

    @Override
    public SortedSet<String> list(String s, String s1, boolean b, boolean b1, boolean b2) throws IOException {
        return null;
    }

    @Override
    public SortedSet<String> describeLocations() {
        return null;
    }
}
