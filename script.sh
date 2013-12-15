rm -rf /tmp/appfuse-gwt
mkdir /tmp/appfuse-gwt
git archive master | tar -x -C /tmp/appfuse-gwt
gedit /tmp/appfuse-gwt/pom.xml /tmp/appfuse-gwt/web/pom.xml /tmp/appfuse-gwt/archetypes/pom.xml /tmp/appfuse-gwt/archetypes/build.xml
cd /tmp/appfuse-gwt
mvn clean deploy -P gwtDebug,itest -DaltDeploymentRepository=local-repo::default::file:/home/opt/workspaces/AppFuse/repo-2.2.2

