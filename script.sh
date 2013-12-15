#!/bin/bash

VERSION=2.2.2-SNAPSHOT
rm -rf /tmp/appfuse-gwt
mkdir /tmp/appfuse-gwt
git archive master | tar -x -C /tmp/appfuse-gwt
gedit /tmp/appfuse-gwt/pom.xml /tmp/appfuse-gwt/web/pom.xml /tmp/appfuse-gwt/archetypes/pom.xml /tmp/appfuse-gwt/archetypes/build.xml
mkdir -p /home/opt/workspaces/AppFuse/repo-${VERSION}
cd /tmp/appfuse-gwt
mvn clean deploy -P gwtDebug,itest -DaltDeploymentRepository=local-repo::default::file:/home/opt/workspaces/AppFuse/repo-${VERSION} && \
cd /tmp
mvn archetype:generate -B \
    -DarchetypeGroupId=org.appfuse.archetypes \
    -DarchetypeArtifactId=appfuse-basic-gwt-archetype \
    -DarchetypeVersion=${VERSION} \
    -DgroupId=com.mycompany \
    -DartifactId=myproject \
    -DarchetypeRepository=file:/home/opt/workspaces/AppFuse/repo-${VERSION} && \
cd myproject
mvn clean install -P gwtDebug,itest && \
mvn appfuse:full-source && \
mvn clean install -P gwtDebug,itest

