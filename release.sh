ant dist
mkdir dist;mkdir dist/release
cp ../appfuse*.zip dist/release

cd ..
rm -rf appfuse-spring-dist
mkdir appfuse-spring-dist
cp -r appfuse appfuse-spring-dist/.
cd appfuse-spring-dist/appfuse
rm -r extras/myjavapack
cd extras/spring;ant install;cd ../..;rm -r extras/spring
ant dist -Darchive.prefix=appfuse-springmvc -Dnodocs=true
cp ../*.zip ../../appfuse/dist/release

cd ../..
rm -rf appfuse-webwork-dist
mkdir appfuse-webwork-dist
cp -r appfuse appfuse-webwork-dist/.
cd appfuse-webwork-dist/appfuse
rm -r extras/myjavapack
cd extras/webwork;ant install;cd ../..;rm -r extras/webwork
ant dist -Darchive.prefix=appfuse-webwork -Dnodocs=true
cp ../*.zip ../../appfuse/dist/release

cd ../..
rm -rf appfuse-jsf-dist
mkdir appfuse-jsf-dist
cp -r appfuse appfuse-jsf-dist/.
cd appfuse-jsf-dist/appfuse
rm -r extras/myjavapack
cd extras/jsf;ant install;cd ../..;rm -r extras/jsf
ant dist -Darchive.prefix=appfuse-jsf -Dnodocs=true
cp ../*.zip ../../appfuse/dist/release

cd ../..
rm -rf appfuse-tapestry-dist
mkdir appfuse-tapestry-dist
cp -r appfuse appfuse-tapestry-dist/.
cd appfuse-tapestry-dist/appfuse
rm -r extras/myjavapack
cd extras/tapestry;ant install;cd ../..;rm -r extras/tapestry
ant dist -Darchive.prefix=appfuse-tapestry -Dnodocs=true
cp ../*.zip ../../appfuse/dist/release

