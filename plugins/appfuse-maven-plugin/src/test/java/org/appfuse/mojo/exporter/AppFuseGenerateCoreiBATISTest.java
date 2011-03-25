package org.appfuse.mojo.exporter;

import org.appfuse.mojo.AbstractAppFuseMojoTestCase;
import org.apache.maven.project.MavenProject;

public final class AppFuseGenerateCoreiBATISTest extends AbstractAppFuseMojoTestCase {

    public void testGenerateCoreWithoutWeb() throws Exception {
        deleteDirectory("target/appfuse/generated");
        super.setGenericCore(false);
        getHibernateMojo("gen-core", "annotationconfiguration").execute();

        assertTrue("can't find PersonManager-bean.xml",
                checkExists("target/appfuse/generated/src/main/resources/PersonManager-bean.xml"));

        assertTrue("can't find PersonSQL.xml",
                checkExists("target/appfuse/generated/src/main/resources/sqlmaps/PersonSQL.xml"));

        assertTrue("can't find compass-gps.xml",
                checkExists("target/appfuse/generated/src/main/resources/compass-gps.xml"));

        assertTrue("can't find Person-select-ids.xml",
                checkExists("target/appfuse/generated/src/main/resources/Person-select-ids.xml"));

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

    @Override
    protected MavenProject getMavenProject() {
        MavenProject project = super.getMavenProject();
        project.getProperties().setProperty("dao.framework", "ibatis");
        return project;
    }
}
