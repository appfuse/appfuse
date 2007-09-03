package org.appfuse.mojo.exporter;

import org.appfuse.mojo.AbstractAppFuseMojoTestCase;

public final class TableGeneratorMojoTest extends AbstractAppFuseMojoTestCase {

    public void testGenerateGenericBeanDefinitions() throws Exception {
        deleteDirectory("target/appfuse/generated");
        super.setGenericCore(true);
        getHibernateMojo("gen", "annotationconfiguration").execute();

        assertTrue("can't find PersonManager-bean.xml",
                checkExists("target/appfuse/generated/src/main/resources/PersonManager-bean.xml"));
    }

    public void testGenerateDao() throws Exception {
        deleteDirectory("target/appfuse/generated");
        getHibernateMojo("gen", "annotationconfiguration").execute();

        assertTrue("can't find PersonDao.java",
                checkExists("target/appfuse/generated/src/main/java/annotationconfiguration/dao/PersonDao.java"));
    }

    public void testGenerateManager() throws Exception {
        deleteDirectory("target/appfuse/generated");
        getHibernateMojo("gen", "annotationconfiguration").execute();

        assertTrue("can't find PersonManagerImplTest.java",
                checkExists("target/appfuse/generated/src/test/java/annotationconfiguration/service/impl/PersonManagerImplTest.java"));

        assertTrue("can't find PersonManager.java",
                checkExists("target/appfuse/generated/src/main/java/annotationconfiguration/service/PersonManager.java"));

        assertTrue("can't find PersonManagerImpl.java",
                checkExists("target/appfuse/generated/src/main/java/annotationconfiguration/service/impl/PersonManagerImpl.java"));

    }

    @Override
    protected void setUp() throws Exception {
        System.setProperty("entity", "Person");
        System.setProperty("type", "table");
        super.setUp();
    }
}
