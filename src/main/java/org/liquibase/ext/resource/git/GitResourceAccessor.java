package org.liquibase.ext.resource.git;

import liquibase.resource.AbstractResourceAccessor;
import liquibase.resource.InputStreamList;
import liquibase.util.StringUtil;
import org.eclipse.jgit.lib.Repository;

import java.io.IOException;
import java.util.SortedSet;
import java.util.TreeSet;

public class GitResourceAccessor extends AbstractResourceAccessor {

    private Repository gitRepository;

    public GitResourceAccessor() {}

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    /**
     *
     * @param   relativeTo           IGNORED
     * @param   streamPath           A path which contains s3://<bucket name>/<key>
     * @return  InputStreamList      A list of InputStream read from S3
     * @throws  IOException          Thrown if the path is invalid
     *
     */
    @Override
    public InputStreamList openStreams(String relativeTo, String streamPath) throws IOException {
        if (StringUtil.isEmpty(streamPath)) {
            return null;
        }
        if (! streamPath.endsWith("git://")) {
            return null;
        }

        // do git stuff

        System.out.println(streamPath);

        return new InputStreamList();

//        String editedStreamPath = streamPath.replace("git://", "");
//        Path path = Paths.get(editedStreamPath);
//        String bucketName = path.getName(0).toString();
//        String key = path.subpath(1, path.getNameCount()).toString();
//        GetObjectRequest getObjectRequest =
//                GetObjectRequest.builder()
//                        .bucket(bucketName)
//                        .key(key)
//                        .build();
//        ResponseInputStream<GetObjectResponse> responseInputStream = s3Client.getObject(getObjectRequest);
//        try {
//            return new InputStreamList(new URI(streamPath), responseInputStream);
//        } catch (URISyntaxException use) {
//            throw new IOException(use);
//        }
    }

    @Override
    public SortedSet<String> list(String s, String s1, boolean b, boolean b1, boolean b2) throws IOException {
        return null;
    }

    @Override
    public SortedSet<String> describeLocations() {
        TreeSet<String> description = new TreeSet<>();
        description.add("git");
        return description;
    }
}
