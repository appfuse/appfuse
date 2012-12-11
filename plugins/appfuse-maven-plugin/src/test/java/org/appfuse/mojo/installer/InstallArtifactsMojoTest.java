package org.appfuse.mojo.installer;

import org.appfuse.mojo.AbstractAppFuseMojoTestCase;

public class InstallArtifactsMojoTest extends AbstractAppFuseMojoTestCase {

    public void testCreateStrutsProject() throws Exception {
        createTestProject("appfuse-basic-struts-archetype", "2.2.2-SNAPSHOT");
        assertTrue("can't find test-project's pom.xml", checkExists("target/test-project/pom.xml"));
    }
}
