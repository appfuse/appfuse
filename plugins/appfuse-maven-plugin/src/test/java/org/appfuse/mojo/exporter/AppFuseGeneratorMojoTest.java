package org.appfuse.mojo.exporter;

import org.appfuse.mojo.HibernateExporterMojo;
import org.appfuse.mojo.AbstractAppFuseMojoTestCase;

public final class AppFuseGeneratorMojoTest extends AbstractAppFuseMojoTestCase {

    public void testGenerateGenericBeanDefinitions() throws Exception {
        deleteDirectory("target/appfuse/generated");
        super.setGenericCore(true);
        getHibernateMojo("gen-ui", "annotationconfiguration").execute();

        assertTrue("can't find PersonManager-bean.xml",
                checkExists("target/appfuse/generated/src/main/resources/PersonManager-bean.xml"));
    }

    public void testGenerateDao() throws Exception {
        deleteDirectory("target/appfuse/generated");
        getHibernateMojo("gen-ui", "annotationconfiguration").execute();

        assertTrue("can't find PersonDao.java",
                checkExists("target/appfuse/generated/src/main/java/annotationconfiguration/dao/PersonDao.java"));
    }

    public void testGenerateManager() throws Exception {
        deleteDirectory("target/appfuse/generated");
        getHibernateMojo("gen-ui", "annotationconfiguration").execute();

        assertTrue("can't find PersonManagerImplTest.java",
                checkExists("target/appfuse/generated/src/test/java/annotationconfiguration/service/impl/PersonManagerImplTest.java"));

        assertTrue("can't find PersonManager.java",
                checkExists("target/appfuse/generated/src/main/java/annotationconfiguration/service/PersonManager.java"));

        assertTrue("can't find PersonManagerImpl.java",
                checkExists("target/appfuse/generated/src/main/java/annotationconfiguration/service/impl/PersonManagerImpl.java"));

    }

    public void testGenerateJSF() throws Exception {
        deleteDirectory("target/appfuse/generated");
        HibernateExporterMojo mojo = getHibernateMojo("gen-ui", "annotationconfiguration");
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

    public void testGenerateSpring() throws Exception {
        deleteDirectory("target/appfuse/generated");
        HibernateExporterMojo mojo = getHibernateMojo("gen-ui", "annotationconfiguration");
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

        assertTrue("can't find Persons.jsp",
                checkExists("target/appfuse/generated/src/main/webapp/WEB-INF/pages/Persons.jsp"));

        assertTrue("can't find PersonForm.jsp",
                checkExists("target/appfuse/generated/src/main/webapp/WEB-INF/pages/PersonForm.jsp"));

        assertTrue("can't find Person-beans.xml",
                checkExists("target/appfuse/generated/src/main/webapp/WEB-INF/Person-beans.xml"));

        assertTrue("can't find ApplicationResources.properties",
                checkExists("target/appfuse/generated/src/main/resources/Person-ApplicationResources.properties"));

        assertTrue("can't find web-tests.xml",
                checkExists("target/appfuse/generated/src/test/resources/Person-web-tests.xml"));

    }
    
    public void testGenerateStruts() throws Exception {
        deleteDirectory("target/appfuse/generated");
        getHibernateMojo("gen-ui", "annotationconfiguration").execute();

        assertTrue("can't find PersonActionTest.java",
                checkExists("target/appfuse/generated/src/test/java/annotationconfiguration/webapp/action/PersonActionTest.java"));

        assertTrue("can't find PersonAction.java",
                checkExists("target/appfuse/generated/src/main/java/annotationconfiguration/webapp/action/PersonAction.java"));

        assertTrue("can't find PersonList.jsp",
                checkExists("target/appfuse/generated/src/main/webapp/WEB-INF/pages/PersonList.jsp"));

        assertTrue("can't find PersonForm.jsp",
                checkExists("target/appfuse/generated/src/main/webapp/WEB-INF/pages/PersonForm.jsp"));

        assertTrue("can't find Person-beans.xml",
                checkExists("target/appfuse/generated/src/main/webapp/WEB-INF/Person-struts-bean.xml"));

        assertTrue("can't find Person-struts.xml",
                checkExists("target/appfuse/generated/src/main/resources/Person-struts.xml"));

        assertTrue("can't find ApplicationResources.properties",
                checkExists("target/appfuse/generated/src/main/resources/Person-ApplicationResources.properties"));

        assertTrue("can't find web-tests.xml",
                checkExists("target/appfuse/generated/src/test/resources/Person-web-tests.xml"));
    }

    public void testGenerateTapestry() throws Exception {
        deleteDirectory("target/appfuse/generated");
        HibernateExporterMojo mojo = getHibernateMojo("gen-ui", "annotationconfiguration");
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
}