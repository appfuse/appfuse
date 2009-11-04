package org.appfuse.mojo.exporter;

import org.appfuse.mojo.AbstractAppFuseMojoTestCase;
import org.appfuse.mojo.HibernateExporterMojo;

public final class GenerateJSFTest extends AbstractAppFuseMojoTestCase {

    public void testGenerateCodeForPerson() throws Exception {
        final String POJO_NAME = "Person";
        System.setProperty("entity", POJO_NAME);
        deleteDirectory("target/appfuse/generated");
        HibernateExporterMojo mojo = getHibernateMojo("gen", "annotationconfiguration");
        mojo.getProject().getProperties().setProperty("web.framework", "jsf");
        mojo.execute();

        assertTrue("can't find " + POJO_NAME + "FormTest.java",
                checkExists("target/appfuse/generated/src/test/java/annotationconfiguration/webapp/action/" + POJO_NAME + "FormTest.java"));

        assertTrue("can't find /" + POJO_NAME + "Form.java",
                checkExists("target/appfuse/generated/src/main/java/annotationconfiguration/webapp/action/" + POJO_NAME + "Form.java"));

        assertTrue("can't find " + POJO_NAME + "ListTest.java",
                checkExists("target/appfuse/generated/src/test/java/annotationconfiguration/webapp/action/" + POJO_NAME + "ListTest.java"));

        assertTrue("can't find /" + POJO_NAME + "List.java",
                checkExists("target/appfuse/generated/src/main/java/annotationconfiguration/webapp/action/" + POJO_NAME + "List.java"));

        assertTrue("can't find " + POJO_NAME + "s.xhtml",
                checkExists("target/appfuse/generated/src/main/webapp/" + POJO_NAME + "s.xhtml"));

        assertTrue("can't find " + POJO_NAME + "Form.xhtml",
                checkExists("target/appfuse/generated/src/main/webapp/" + POJO_NAME + "Form.xhtml"));

        // JSF managed beans configured by Spring annotations in 2.1+
        assertFalse("found " + POJO_NAME + "-managed-beans.xml",
                checkExists("target/appfuse/generated/src/main/webapp/WEB-INF/" + POJO_NAME + "-managed-beans.xml"));

        assertTrue("can't find " + POJO_NAME + "-navigation.xml",
                checkExists("target/appfuse/generated/src/main/webapp/WEB-INF/" + POJO_NAME + "-navigation.xml"));

        assertTrue("can't find ApplicationResources.properties",
                checkExists("target/appfuse/generated/src/main/resources/" + POJO_NAME + "-ApplicationResources.properties"));

        assertTrue("can't find web-tests.xml",
                checkExists("target/appfuse/generated/src/test/resources/" + POJO_NAME + "-web-tests.xml"));

    }
    
    public void testGenerateCodeForFicheiro() throws Exception {
        final String POJO_NAME = "Ficheiro";
        System.setProperty("entity", POJO_NAME);
        deleteDirectory("target/appfuse/generated");
        HibernateExporterMojo mojo = getHibernateMojo("gen", "annotationconfiguration");
        mojo.getProject().getProperties().setProperty("web.framework", "jsf");
        mojo.execute();

        assertTrue("can't find " + POJO_NAME + "FormTest.java",
                checkExists("target/appfuse/generated/src/test/java/annotationconfiguration/webapp/action/" + POJO_NAME + "FormTest.java"));

        assertTrue("can't find /" + POJO_NAME + "Form.java",
                checkExists("target/appfuse/generated/src/main/java/annotationconfiguration/webapp/action/" + POJO_NAME + "Form.java"));

        assertTrue("can't find " + POJO_NAME + "ListTest.java",
                checkExists("target/appfuse/generated/src/test/java/annotationconfiguration/webapp/action/" + POJO_NAME + "ListTest.java"));

        assertTrue("can't find /" + POJO_NAME + "List.java",
                checkExists("target/appfuse/generated/src/main/java/annotationconfiguration/webapp/action/" + POJO_NAME + "List.java"));

        assertTrue("can't find " + POJO_NAME + "s.xhtml",
                checkExists("target/appfuse/generated/src/main/webapp/" + POJO_NAME + "s.xhtml"));

        assertTrue("can't find " + POJO_NAME + "Form.xhtml",
                checkExists("target/appfuse/generated/src/main/webapp/" + POJO_NAME + "Form.xhtml"));

        // JSF managed beans configured by Spring annotations in 2.1+
        assertFalse("found " + POJO_NAME + "-managed-beans.xml",
                checkExists("target/appfuse/generated/src/main/webapp/WEB-INF/" + POJO_NAME + "-managed-beans.xml"));

        assertTrue("can't find " + POJO_NAME + "-navigation.xml",
                checkExists("target/appfuse/generated/src/main/webapp/WEB-INF/" + POJO_NAME + "-navigation.xml"));

        assertTrue("can't find ApplicationResources.properties",
                checkExists("target/appfuse/generated/src/main/resources/" + POJO_NAME + "-ApplicationResources.properties"));

        assertTrue("can't find web-tests.xml",
                checkExists("target/appfuse/generated/src/test/resources/" + POJO_NAME + "-web-tests.xml"));
    }

    public void testGenerateCodeForEvento() throws Exception {
        final String POJO_NAME = "Evento";
        System.setProperty("entity", POJO_NAME);
        deleteDirectory("target/appfuse/generated");
        HibernateExporterMojo mojo = getHibernateMojo("gen", "annotationconfiguration");
        mojo.getProject().getProperties().setProperty("web.framework", "jsf");
        mojo.execute();

        assertTrue("can't find " + POJO_NAME + "s.xhtml",
                checkExists("target/appfuse/generated/src/main/webapp/" + POJO_NAME + "s.xhtml"));
    }

    @Override
    protected void setUp() throws Exception {
        System.setProperty("type", "pojo");
        super.setUp();
    }
}
