package org.appfuse.mojo.installer;

import org.appfuse.mojo.AbstractAppFuseMojoTestCase;

public class InstallArtifactsMojoTest extends AbstractAppFuseMojoTestCase {

    public void testCreateStrutsProject() throws Exception {
        createTestProject("appfuse-basic-struts-archetype", "3.5.0");
        assertTrue("can't find test-project's pom.xml", checkExists("target/test-project/pom.xml"));
    }
}
