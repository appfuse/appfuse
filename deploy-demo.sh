# default Struts version
ant remove install -Dtomcat.server=drevil -Dhttp.port=80
#ant test-canoo -Dtomcat.server=drevil
# Spring + iBATIS
rm -r ../appfuse-spring
ant new -Dapp.name=appfuse-spring -Ddb.name=ibatis
cd ../appfuse-spring
ant install-ibatis install-springmvc
cd extras/ibatis
ant uninstall-hibernate
cd ../..
ant clean remove install -Dtomcat.server=drevil -Dhttp.port=80
#ant test-canoo -Dtomcat.server=drevil
# WebWork + Spring + Hibernate
rm -r ../appfuse-webwork
ant new -Dapp.name=appfuse-webwork -Ddb.name=webwork
cd ../appfuse-webwork
ant install-webwork
ant clean remove install -Dtomcat.server=drevil -Dhttp.port=80
#ant test-canoo -Dtomcat.server=drevil
# Deploy Javadocs
cd ~/appfuse
ant javadoc
cd build/docs/api
jar cvf api.jar *
scp api.jar raible@raibledesigns.com:~/.
ssh raible@raibledesigns.com
cd public_html/downloads/appfuse/api/.
rm -r *
mv ~/api.jar .
jar xvf api.jar
rm api.jar
exit