package org.appfuse.mojo.exporter;

import org.appfuse.mojo.AbstractAppFuseMojoTestCase;
import org.apache.maven.project.MavenProject;

public final class AppFuseGenerateCoreJPATest extends AbstractAppFuseMojoTestCase {

    public void testGenerateCoreWithoutWeb() throws Exception {
        deleteDirectory("target/appfuse/generated");
        super.setGenericCore(false);
        getHibernateMojo("gen-core", "jpaconfiguration").execute();
        // jpaconfiguration throws can't find persistence unit: null if the above is changed to jpaconfiguraiton

        assertTrue("can't find PersonManager-bean.xml",
                checkExists("target/appfuse/generated/src/main/resources/PersonManager-bean.xml"));
        
        // make sure web files don't get generated
        assertFalse("found web-tests.xml",
                checkExists("target/appfuse/generated/src/test/resources/Person-web-tests.xml"));
    }

    @Override
    protected void setUp() throws Exception {
        System.setProperty("entity.check", "false");
        System.setProperty("entity", "Person");
        System.setProperty("type", "pojo");
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        System.clearProperty("entity.check");
        super.tearDown();
    }

    @Override
    protected MavenProject getMavenProject() {
        MavenProject project = super.getMavenProject();
        project.getProperties().setProperty("dao.framework", "jpa");
        return project;
    }
}
