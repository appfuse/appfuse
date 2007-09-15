package org.appfuse.mojo.exporter;

import org.appfuse.mojo.AbstractAppFuseMojoTestCase;
import org.apache.maven.plugin.MojoFailureException;

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

    public void testGenerateCodeForInvalidEntity() throws Exception {
        System.setProperty("entity", "Clown");
        deleteDirectory("target/appfuse/generated");

        try {
            getHibernateMojo("gen-web", "annotationconfiguration").execute();
            fail("Mojo did not fail when invalid entity configured.");
        } catch (MojoFailureException e) {
            assertEquals("[ERROR] The 'Clown' entity does not exist in 'src/main/java/com/company/model'.", e.getMessage());
            assertNotNull(e);
        }
    }

    public void testGenerateCodeForEntityWithNoEntityAnnotation() throws Exception {
        System.setProperty("entity", "Dog");
        deleteDirectory("target/appfuse/generated");

        try {
            getHibernateMojo("gen-web", "annotationconfiguration").execute();
            fail("Mojo did not fail when invalid entity configured.");
        } catch (MojoFailureException e) {
            assertEquals("[ERROR] The 'Dog' entity does not exist in 'src/main/java/com/company/model'.", e.getMessage());
            assertNotNull(e);
        }
    }

    @Override
    protected void setUp() throws Exception {
        System.setProperty("entity", "Person");
        System.setProperty("type", "pojo");
        super.setUp();
    }
}
