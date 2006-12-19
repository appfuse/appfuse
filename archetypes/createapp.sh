#mvn
appname=mywebapp
#rm -r $appname 
mvn archetype:create -DarchetypeGroupId=org.appfuse -DarchetypeArtifactId=appfuse-archetype-basic -DarchetypeVersion=1.0-SNAPSHOT -DgroupId=com.raibledesigns -DartifactId=$appname -DremoteRepositories=http://static.appfuse.org/repository
#sed 's/>${web.framework}</>$struts</g' $appname/pom.xml > $appname/pom.xml.new
#mv $appname/pom.xml.new $appname/pom.xml
