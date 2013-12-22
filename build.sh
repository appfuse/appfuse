#!/bin/bash

function log {
	 echo $(date) $* >> /tmp/appfuse-gwt/build.log
}

function checkStatus {
    status=$?
    if [ $status -ne 0 ]; then
        exit $status
    fi
}

VERSION=3.0.0
rm -rf /tmp/appfuse-gwt
mkdir /tmp/appfuse-gwt

log "Building version ${VERSION}..."
log "Exporting git repository..."
git archive master | tar -x -C /tmp/appfuse-gwt
cd /tmp/appfuse-gwt

log "Replacing deploy repository urls..."
mkdir -p /home/opt/workspaces/AppFuse/repo-${VERSION}
sed -i -e "s:http\://oss.sonatype.org/service/local/staging/deploy/maven2:file\:/home/opt/workspaces/AppFuse/repo-${VERSION}:g" /tmp/appfuse-gwt/archetypes/build.xml
find /tmp/appfuse-gwt -name pom.xml -exec sed -i -e "s:http\://oss.sonatype.org/service/local/staging/deploy/maven2:file\:/home/opt/workspaces/AppFuse/repo-${VERSION}:g" {} \;

log "Disabling non gwt modules..."
sed -i -e "s:^\s*<module>plugins</module>:<\!-- & -->:g" pom.xml
sed -i -e "s:^\s*<module>archetypes</module>:<\!-- & -->:g" pom.xml

DISABLED_WEB="jsf spring struts tapestry wicket"
for module in $DISABLED_WEB ; do
	sed -i -e "s:^\s*<module>$module</module>:<\!-- & -->:g" web/pom.xml
done
DISABLED_ARCHETYPES="appfuse-core appfuse-basic-jsf appfuse-basic-spring appfuse-basic-struts appfuse-basic-tapestry appfuse-basic-wicket \
appfuse-modular-jsf appfuse-modular-struts appfuse-modular-spring appfuse-modular-tapestry appfuse-modular-wicket appfuse-ws"
for module in $DISABLED_ARCHETYPES ; do
	sed -i -e "s:^\s*<module>$module</module>:<\!-- & -->:g" archetypes/pom.xml
done
sed -i -e 's/-Pitest/-Pitest,gwtDebug/g' archetypes/build.xml


log "mvn clean install..."
mvn clean install -P gwtDebug,itest 
checkStatus

log "mvn clean deploy archetypes..."
cd archetypes
mvn clean deploy -P gwtDebug,itest 
checkStatus

log "Generating project from basic archetype..."
rm -rf /tmp/my-basic-gwt-project
cd /tmp/
mvn archetype:generate -B \
    -DarchetypeGroupId=org.appfuse.archetypes \
    -DarchetypeArtifactId=appfuse-basic-gwt-archetype \
    -DarchetypeVersion=${VERSION} \
    -DgroupId=com.mycompany \
    -DartifactId=my-basic-gwt-project \
    -DarchetypeRepository=file:/home/opt/workspaces/AppFuse/repo-${VERSION}
checkStatus

log "Testing generated project..."
cd /tmp/my-basic-gwt-project
mvn clean install -P gwtDebug,itest
checkStatus
log "Downloading appfuse full-source..."
mvn appfuse:full-source
echo mail.port=25250 >> src/test/resources/mail.properties
log "Testing generated full-source project..."
mvn clean install -P gwtDebug,itest
checkStatus

log "Generating project from modular archetype..."
rm -rf /tmp/my-modular-gwt-project
cd /tmp/
mvn archetype:generate -B \
    -DarchetypeGroupId=org.appfuse.archetypes \
    -DarchetypeArtifactId=appfuse-modular-gwt-archetype \
    -DarchetypeVersion=${VERSION} \
    -DgroupId=com.mycompany \
    -DartifactId=my-modular-gwt-project \
    -DarchetypeRepository=file:/home/opt/workspaces/AppFuse/repo-${VERSION}
checkStatus

log "Testing generated project..."
cd /tmp/my-modular-gwt-project
mvn clean install -P gwtDebug,itest
checkStatus
log "Downloading appfuse full-source..."
mvn appfuse:full-source
echo mail.port=25250 >> core/src/test/resources/mail.properties
log "Testing generated full-source project..."
mvn clean install -P gwtDebug,itest
checkStatus

log "Build finished."


