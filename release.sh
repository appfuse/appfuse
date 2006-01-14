ant dist

cd ..
rm -rf appfuse-spring-dist
mkdir appfuse-spring-dist
cp -r appfuse appfuse-spring-dist/.
cd appfuse-spring-dist/appfuse
cd extras/spring;ant install;cd ../..
ant dist -Darchive.prefix=appfuse-springmvc

cd ../..
rm -rf appfuse-webwork-dist
mkdir appfuse-webwork-dist
cp -r appfuse appfuse-webwork-dist/.
cd appfuse-webwork-dist/appfuse
cd extras/webwork;ant install;cd ../..
ant dist -Darchive.prefix=appfuse-webwork

cd ../..
rm -rf appfuse-jsf-dist
mkdir appfuse-jsf-dist
cp -r appfuse appfuse-jsf-dist/.
cd appfuse-jsf-dist/appfuse
cd extras/jsf;ant install;cd ../..
ant dist -Darchive.prefix=appfuse-jsf

cd ../..
rm -rf appfuse-tapestry-dist
mkdir appfuse-tapestry-dist
cp -r appfuse appfuse-tapestry-dist/.
cd appfuse-tapestry-dist/appfuse
cd extras/tapestry;ant install;cd ../..
ant dist -Darchive.prefix=appfuse-tapestry