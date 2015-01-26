package org.appfuse.mojo.exporter;

import org.appfuse.mojo.AbstractAppFuseMojoTestCase;
import org.appfuse.mojo.HibernateExporterMojo;

public final class GenerateStrutsTest extends AbstractAppFuseMojoTestCase {

    public void testGenerateStruts() throws Exception {
        deleteDirectory("target/appfuse/generated");
        getHibernateMojo("gen", "annotationconfiguration").execute();

        assertTrue("can't find PersonActionTest.java",
                checkExists("target/appfuse/generated/src/test/java/annotationconfiguration/webapp/action/PersonActionTest.java"));

        assertTrue("can't find PersonAction.java",
                checkExists("target/appfuse/generated/src/main/java/annotationconfiguration/webapp/action/PersonAction.java"));

        assertTrue("can't find /PersonWebTest.java",
                checkExists("target/appfuse/generated/src/test/java/annotationconfiguration/webapp/PersonWebTest.java"));

        assertTrue("can't find PersonList.jsp",
                checkExists("target/appfuse/generated/src/main/webapp/WEB-INF/pages/PersonList.jsp"));

        assertTrue("can't find PersonForm.jsp",
                checkExists("target/appfuse/generated/src/main/webapp/WEB-INF/pages/PersonForm.jsp"));

        assertTrue("can't find Person-beans.xml",
                checkExists("target/appfuse/generated/src/main/webapp/WEB-INF/Person-struts-bean.xml"));

        assertTrue("can't find Person-struts.xml",
                checkExists("target/appfuse/generated/src/main/resources/Person-struts.xml"));

        assertTrue("can't find Person-validation.xml",
                checkExists("target/appfuse/generated/src/main/resources/annotationconfiguration/model/Person-validation.xml"));

        assertTrue("can't find PersonAction-validation.xml",
                checkExists("target/appfuse/generated/src/main/resources/annotationconfiguration/webapp/action/PersonAction-validation.xml"));

        assertTrue("can't find ApplicationResources.properties",
                checkExists("target/appfuse/generated/src/main/resources/Person-ApplicationResources.properties"));

        assertTrue("can't find web-tests.xml",
                checkExists("target/appfuse/generated/src/test/resources/Person-web-tests.xml"));
    }

    public void testGenerateGenericFollowedbyNonGeneric() throws Exception {
        deleteDirectory("target/appfuse/generated");
        super.setGenericCore(true);
        getHibernateMojo("gen", "annotationconfiguration").execute();

        assertTrue("can't find Person-struts.xml",
                checkExists("target/appfuse/generated/src/main/resources/Person-struts.xml"));

        super.setGenericCore(false);
        getHibernateMojo("gen", "annotationconfiguration").execute();

        assertTrue("can't find Person-struts.xml",
                checkExists("target/appfuse/generated/src/main/resources/Person-struts.xml"));
    }

    @Override
    protected void setUp() throws Exception {
        System.setProperty("entity", "Person");
        System.setProperty("type", "pojo");
        super.setUp();
    }
}
