ant dist
cd ..
rm -rf appfuse-spring-dist
mkdir appfuse-spring-dist
cp -r appfuse appfuse-spring-dist/.
cd appfuse-spring-dist/appfuse
ant install-springmvc
rm -r extras/spring
ant dist -Darchive.prefix=appfuse-springmvc

cd ../..
rm -rf appfuse-webwork-dist
mkdir appfuse-webwork-dist
cp -r appfuse appfuse-webwork-dist/.
cd appfuse-webwork-dist/appfuse
ant install-webwork 
rm -rf extras/webwork
ant dist -Darchive.prefix=appfuse-webwork