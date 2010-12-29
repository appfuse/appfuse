package org.appfuse.mojo.exporter;

import org.appfuse.mojo.AbstractAppFuseMojoTestCase;

public final class AppFuseGeneratorSubPackageMojoTest extends AbstractAppFuseMojoTestCase {

    public void testGenerateDao() throws Exception {
        deleteDirectory("target/appfuse/generated");
        getHibernateMojo("gen", "annotationconfiguration").execute();

        assertTrue("can't find ItemDao.java",
                checkExists("target/appfuse/generated/src/main/java/org/foo/sub/dao/ItemDao.java"));
    }

    public void testGenerateManager() throws Exception {
        deleteDirectory("target/appfuse/generated");
        getHibernateMojo("gen", "annotationconfiguration").execute();

        assertTrue("can't find ItemManagerImplTest.java",
                checkExists("target/appfuse/generated/src/test/java/org/foo/sub/service/impl/ItemManagerImplTest.java"));

        assertTrue("can't find ItemManager.java",
                checkExists("target/appfuse/generated/src/main/java/org/foo/sub/service/ItemManager.java"));

        assertTrue("can't find ItemManagerImpl.java",
                checkExists("target/appfuse/generated/src/main/java/org/foo/sub/service/impl/ItemManagerImpl.java"));

    }

    @Override
    protected void setUp() throws Exception {
        System.setProperty("entity", "org.foo.sub.model.Item");
        System.setProperty("type", "pojo");
        super.setUp();
    }
}