package org.appfuse.mojo.exporter;

import org.appfuse.mojo.AbstractAppFuseMojoTestCase;


public final class ModelGeneratorMojoTest extends AbstractAppFuseMojoTestCase {

    public void testJdbcMojoExecution() throws Exception {
        deleteDirectory("target/appfuse/generated-sources-jdbc");
        getHibernateMojo("gen-model", "jdbcconfiguration").execute();
        assertTrue("can't find Categories.java",
                checkExists("target/appfuse/generated-sources-jdbc/jdbcconfiguration/Categories.java"));
        assertTrue("can't find Forums.java",
                checkExists("target/appfuse/generated-sources-jdbc/jdbcconfiguration/Forums.java"));
    }
}