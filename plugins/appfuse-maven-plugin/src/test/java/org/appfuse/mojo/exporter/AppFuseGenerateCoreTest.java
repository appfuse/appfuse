package org.appfuse.mojo.exporter;

import org.appfuse.mojo.AbstractAppFuseMojoTestCase;
import org.appfuse.mojo.HibernateExporterMojo;

public final class AppFuseGenerateCoreTest extends AbstractAppFuseMojoTestCase {

    public void testGenerateCoreWithoutWeb() throws Exception {
        deleteDirectory("target/appfuse/generated");
        super.setGenericCore(false);
        getHibernateMojo("gen-core", "annotationconfiguration").execute();

        assertTrue("can't find PersonManager-bean.xml",
                checkExists("target/appfuse/generated/src/main/resources/PersonManager-bean.xml"));

        // make sure web files don't get generated
        assertFalse("found web-tests.xml",
                checkExists("target/appfuse/generated/src/test/resources/Person-web-tests.xml"));
    }

    @Override
    protected void setUp() throws Exception {
        System.setProperty("entity", "Person");
        System.setProperty("type", "pojo");
        super.setUp();
    }
}
