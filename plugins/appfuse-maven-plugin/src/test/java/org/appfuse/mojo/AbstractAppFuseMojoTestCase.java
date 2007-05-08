package org.appfuse.mojo;

import org.apache.maven.archetype.Archetype;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.repository.DefaultArtifactRepository;
import org.apache.maven.artifact.repository.layout.ArtifactRepositoryLayout;
import org.apache.maven.model.Build;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.apache.maven.plugin.testing.stubs.MavenProjectStub;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.FileUtils;
import org.appfuse.mojo.installer.InstallArtifactsMojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public abstract class AbstractAppFuseMojoTestCase extends AbstractMojoTestCase {
    private boolean genericCore = false;

    public void setGenericCore(boolean genericCore) {
        this.genericCore = genericCore;
    }

    protected boolean checkExists(String target) {
        return (getTestFile(target).exists());
    }

    protected void deleteDirectory(String targetFolder) throws Exception {
        FileUtils.deleteDirectory(getTestFile(targetFolder));
    }

    protected HibernateExporterMojo getHibernateMojo(String goal, String implementation) throws Exception {
        String path = "target/test-classes/" + implementation + "/" + goal + "-config.xml";
        if (genericCore) {
            path = "target/test-classes/" + implementation + "/generic-config.xml";
        }
        HibernateExporterMojo mojo = (HibernateExporterMojo) lookupMojo(goal, getTestFile(path));
        mojo.getLog().info("executing: " + getTestFile(path).getPath());
        setVariableValueToObject(mojo, "project", getMavenProject());

        return mojo;
    }

    protected InstallArtifactsMojo getInstallMojo(String goal, String implementation) throws Exception {
        String path = "target/test-project";
        InstallArtifactsMojo mojo = (InstallArtifactsMojo) lookupMojo(goal, getTestFile(path));
        mojo.getLog().info("executing: " + getTestFile(path).getPath());
        //setVariableValueToObject(mojo, "project", getMavenProject());
        return mojo;
    }

    protected void copyFileToDirectory(String from, String to) throws Exception {
        FileUtils.copyFileToDirectory(getTestFile(from), getTestFile(to));
    }

    protected MavenProject getMavenProject() {
        final Build build = new Build();
        build.setDirectory("target");
        build.setOutputDirectory("target/classes");
        build.setTestOutputDirectory("target/test-classes");

        return new MavenProjectStub() {
            String daoFramework = "hibernate";
            String webFramework = "struts";
            Properties props;
            
            public Build getBuild() {
                return build;
            }

            public String getArtifactId() {
                return "test-project";
            }
            
            public String getGroupId() {
                return "com.company";
            }

            public String getPackaging() {
                return "war";
            }

            @Override
            public Properties getProperties() {
                if (props == null) {
                    props = new Properties();
                    props.put("dao.framework", this.daoFramework);
                    props.put("web.framework", this.webFramework);
                }
                return props;
            }
        };
    }

    protected void createTestProject(String archetypeArtifactId, String archetypeVersion) throws Exception {
        MavenProject project = getMavenProject();
        FileUtils.deleteDirectory(getTestFile("target/" + project.getArtifactId()));

        Map<String, String> parameters = new HashMap<String, String>();

        parameters.put("groupId", project.getGroupId());
        parameters.put("artifactId", project.getArtifactId());
        parameters.put("version", "1.0-SNAPSHOT");
        parameters.put("basedir", getTestFile("target").getAbsolutePath());

        Archetype archetype = (Archetype) lookup(Archetype.ROLE);

        ArtifactRepositoryLayout layout =
                (ArtifactRepositoryLayout) container.lookup(ArtifactRepositoryLayout.ROLE, "default");

        String mavenRepoLocal = "file://" + System.getProperty("user.home") + System.getProperty("file.separator") +
                ".m2" + System.getProperty("file.separator") + "repository";
        ArtifactRepository localRepository = new DefaultArtifactRepository("local", mavenRepoLocal, layout);

        List<ArtifactRepository> remoteRepositories = new ArrayList<ArtifactRepository>();
        /*String mavenRepoRemote = "http://repo1.maven.org/maven2";
        ArtifactRepository remoteRepository = new DefaultArtifactRepository("remote", mavenRepoRemote, layout);

        remoteRepositories.add(remoteRepository);*/

        String archetypeGroupId = "org.appfuse";
        archetype.createArchetype(archetypeGroupId, archetypeArtifactId, archetypeVersion, localRepository,
                remoteRepositories, parameters);
    }
}
