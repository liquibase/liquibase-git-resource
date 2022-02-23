package org.liquibase.ext.resource.git;

import liquibase.resource.CompositeResourceAccessor;
import liquibase.resource.ResourceAccessor;
import liquibase.resource.ResourceAccessorService;
import liquibase.resource.StandardResourceAccessorService;
import liquibase.servicelocator.PrioritizedService;

public class GitResourceAccessorService extends StandardResourceAccessorService implements ResourceAccessorService {

    private CompositeResourceAccessor compositeResourceAccessor;

    public int getPriority() {
        return PrioritizedService.PRIORITY_DEFAULT + 100;
    }

    /**
     *
     * @return  ResourceAccessor    A new ResourceAccessor
     *
     */
    @Override
    public ResourceAccessor getResourceAccessor(ClassLoader classLoader) {
        //
        // We combine the GitAccessor with the current ResourceAccessor to
        // create a new CompositeResourceAccessor.  The current ResourceAccessor
        // is actually a composite itself
        //
        if (compositeResourceAccessor == null) {
            GitResourceAccessor gitResourceAccessor = new GitResourceAccessor();
            ResourceAccessor parentAccessor = super.getResourceAccessor(classLoader);
            compositeResourceAccessor = new CompositeResourceAccessor(parentAccessor, gitResourceAccessor);
        }
        return compositeResourceAccessor;
    }
}
