rm -r ../appfuse-ibatis
ant new -Dapp.name=appfuse-ibatis -Ddb.name=ibatis
cd ../appfuse-ibatis
ant install-ibatis install-springmvc
ant setup test-all test-reports
