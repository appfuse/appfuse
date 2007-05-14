package org.appfuse.mojo.exporter;

import org.appfuse.mojo.AbstractAppFuseMojoTestCase;
import org.appfuse.mojo.HibernateExporterMojo;

public final class GenerateTapestryTest extends AbstractAppFuseMojoTestCase {

    public void testGenerateTapestry() throws Exception {
        deleteDirectory("target/appfuse/generated");
        HibernateExporterMojo mojo = getHibernateMojo("gen", "annotationconfiguration");
        mojo.getProject().getProperties().setProperty("web.framework", "tapestry");
        mojo.execute();

        assertTrue("can't find PersonFormTest.java",
                checkExists("target/appfuse/generated/src/test/java/annotationconfiguration/webapp/pages/PersonFormTest.java"));

        assertTrue("can't find /PersonForm.java",
                checkExists("target/appfuse/generated/src/main/java/annotationconfiguration/webapp/pages/PersonForm.java"));

        assertTrue("can't find PersonListTest.java",
                checkExists("target/appfuse/generated/src/test/java/annotationconfiguration/webapp/pages/PersonListTest.java"));

        assertTrue("can't find /PersonList.java",
                checkExists("target/appfuse/generated/src/main/java/annotationconfiguration/webapp/pages/PersonList.java"));

        assertTrue("can't find PersonList.html",
                checkExists("target/appfuse/generated/src/main/webapp/WEB-INF/tapestry/PersonList.html"));

        assertTrue("can't find PersonList.page",
                checkExists("target/appfuse/generated/src/main/webapp/WEB-INF/tapestry/PersonList.page"));

        assertTrue("can't find PersonForm.html",
                checkExists("target/appfuse/generated/src/main/webapp/WEB-INF/tapestry/PersonForm.html"));

        assertTrue("can't find PersonForm.page",
                checkExists("target/appfuse/generated/src/main/webapp/WEB-INF/tapestry/PersonForm.page"));

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
