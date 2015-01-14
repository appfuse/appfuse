package org.appfuse.mojo.exporter;

import org.appfuse.mojo.AbstractAppFuseMojoTestCase;
import org.appfuse.mojo.HibernateExporterMojo;

public final class GenerateSpringTest extends AbstractAppFuseMojoTestCase {

    public void testGenerateSpring() throws Exception {
        deleteDirectory("target/appfuse/generated");
        HibernateExporterMojo mojo = getHibernateMojo("gen", "annotationconfiguration");
        mojo.getProject().getProperties().setProperty("web.framework", "spring");
        mojo.execute();

        assertTrue("can't find PersonFormControllerTest.java",
                checkExists("target/appfuse/generated/src/test/java/annotationconfiguration/webapp/controller/PersonFormControllerTest.java"));

        assertTrue("can't find /PersonFormController.java",
                checkExists("target/appfuse/generated/src/main/java/annotationconfiguration/webapp/controller/PersonFormController.java"));

        assertTrue("can't find PersonControllerTest.java",
                checkExists("target/appfuse/generated/src/test/java/annotationconfiguration/webapp/controller/PersonControllerTest.java"));

        assertTrue("can't find /PersonController.java",
                checkExists("target/appfuse/generated/src/main/java/annotationconfiguration/webapp/controller/PersonController.java"));

        assertTrue("can't find /PersonWebTest.java",
                checkExists("target/appfuse/generated/src/test/java/annotationconfiguration/webapp/PersonWebTest.java"));

        assertTrue("can't find Persons.jsp",
                checkExists("target/appfuse/generated/src/main/webapp/WEB-INF/pages/Persons.jsp"));

        assertTrue("can't find Personform.jsp",
                checkExists("target/appfuse/generated/src/main/webapp/WEB-INF/pages/Personform.jsp"));

        assertTrue("can't find Person-validation.xml",
                checkExists("target/appfuse/generated/src/main/webapp/WEB-INF/Person-validation.xml"));

        assertTrue("can't find ApplicationResources.properties",
                checkExists("target/appfuse/generated/src/main/resources/Person-ApplicationResources.properties"));

        assertTrue("can't find web-tests.xml",
                checkExists("target/appfuse/generated/src/test/resources/Person-web-tests.xml"));

    }

    @Override
    protected void setUp() throws Exception {
        System.setProperty("entity", "Person");
        System.setProperty("type", "pojo");
        super.setUp();
    }
}
