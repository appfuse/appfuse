package org.appfuse.mojo.exporter;

import org.appfuse.mojo.AbstractAppFuseMojoTestCase;

public final class AppFuseGenerateWebTest extends AbstractAppFuseMojoTestCase {

    public void testGenerateWebWithoutCore() throws Exception {
        deleteDirectory("target/appfuse/generated");
        getHibernateMojo("gen-web", "annotationconfiguration").execute();

        assertFalse("PersonManager-bean.xml was generated",
                checkExists("target/appfuse/generated/src/main/resources/PersonManager-bean.xml"));

        // make sure web files are generated
        assertTrue("found web-tests.xml",
                checkExists("target/appfuse/generated/src/test/resources/Person-web-tests.xml"));
    }

    @Override
    protected void setUp() throws Exception {
        System.setProperty("entity", "Person");
        System.setProperty("type", "pojo");
        super.setUp();
    }
}
