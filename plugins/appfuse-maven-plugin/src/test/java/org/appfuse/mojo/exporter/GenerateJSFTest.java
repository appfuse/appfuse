package org.appfuse.mojo.exporter;

import org.appfuse.mojo.AbstractAppFuseMojoTestCase;
import org.appfuse.mojo.HibernateExporterMojo;

public final class GenerateJSFTest extends AbstractAppFuseMojoTestCase {

    public void testGenerateJSF() throws Exception {
        deleteDirectory("target/appfuse/generated");
        HibernateExporterMojo mojo = getHibernateMojo("gen", "annotationconfiguration");
        mojo.getProject().getProperties().setProperty("web.framework", "jsf");
        mojo.execute();

        assertTrue("can't find PersonFormTest.java",
                checkExists("target/appfuse/generated/src/test/java/annotationconfiguration/webapp/action/PersonFormTest.java"));

        assertTrue("can't find /PersonForm.java",
                checkExists("target/appfuse/generated/src/main/java/annotationconfiguration/webapp/action/PersonForm.java"));

        assertTrue("can't find PersonListTest.java",
                checkExists("target/appfuse/generated/src/test/java/annotationconfiguration/webapp/action/PersonListTest.java"));

        assertTrue("can't find /PersonList.java",
                checkExists("target/appfuse/generated/src/main/java/annotationconfiguration/webapp/action/PersonList.java"));

        assertTrue("can't find Persons.xhtml",
                checkExists("target/appfuse/generated/src/main/webapp/Persons.xhtml"));

        assertTrue("can't find PersonForm.xhtml",
                checkExists("target/appfuse/generated/src/main/webapp/PersonForm.xhtml"));

        assertTrue("can't find Person-managed-beans.xml",
                checkExists("target/appfuse/generated/src/main/webapp/WEB-INF/Person-managed-beans.xml"));

        assertTrue("can't find Person-navigation.xml",
                checkExists("target/appfuse/generated/src/main/webapp/WEB-INF/Person-navigation.xml"));

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
