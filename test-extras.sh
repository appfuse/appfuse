rm -r ../appfuse-spi
ant new -Dapp.name=appfuse-spi -Ddb.name=ibatis
cd ../appfuse-spi
pause
ant install-ibatis install-springmvc
cd extras/ibatis
ant uninstall-hibernate
cd ../..
pause
ant setup test-all test-reports
