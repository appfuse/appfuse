package org.appfuse.mojo.installer;

import org.appfuse.mojo.AbstractAppFuseMojoTestCase;
import org.apache.maven.project.MavenProject;

public class CopyTemplatesMojoTest extends AbstractAppFuseMojoTestCase {

    public void testCopyTemplatesForStrutsAndHibernate() throws Exception {
        getMojo("copy-templates").execute();
        assertTrue("can't find resources/appfuse", checkExists("target/templates/src/test/resources/appfuse"));
        assertTrue("can't find appfuse/dao/hibernate", checkExists("target/templates/src/test/resources/appfuse/dao/hibernate"));
        assertTrue("can't find appfuse/web/struts", checkExists("target/templates/src/test/resources/appfuse/web/struts"));
    }

    public void testCopyTemplatesForSpringAndJPA() throws Exception {
        CopyTemplatesMojo mojo = getMojo("copy-templates");
        MavenProject project = getMavenProject();
        project.getProperties().put("dao.framework", "jpa");
        project.getProperties().put("web.framework", "spring");
        mojo.setProject(project);
        mojo.execute();
        assertTrue("can't find resources/appfuse", checkExists("target/templates/src/test/resources/appfuse"));
        assertTrue("can't find appfuse/dao/jpa", checkExists("target/templates/src/test/resources/appfuse/dao/jpa"));
        assertTrue("can't find appfuse/web/spring", checkExists("target/templates/src/test/resources/appfuse/web/spring"));
    }

    protected CopyTemplatesMojo getMojo(String goal) throws Exception {
        String path = "target/test-classes/" + goal + "-config.xml";
        CopyTemplatesMojo mojo = (CopyTemplatesMojo) lookupMojo(goal, getTestFile(path));
        mojo.getLog().info("executing: " + getTestFile(path).getPath());
        mojo.setProject(getMavenProject());
        return mojo;
    }
}
