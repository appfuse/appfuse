rm -r ../appfuse-spring
ant new -Dapp.name=appfuse-spring -Ddb.name=ibatis
cd ../appfuse-spring
ant install-ibatis install-springmvc
cd extras/ibatis
ant uninstall-hibernate
cd ../..
ant setup 
ant test-all test-reports
