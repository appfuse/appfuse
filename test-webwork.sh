rm -r ../appfuse-webwork
ant new -Dapp.name=appfuse-webwork -Ddb.name=webwork
cd ../appfuse-webwork
ant install-webwork
ant setup test-all test-reports
