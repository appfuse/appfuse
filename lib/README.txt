The libraries in this directory are used in AppFuse to accomplish various
tasks. The list below details each project and what it's used for. For more
information about each project, google it or view its URL in lib.properties.

* ant-contrib
- ant tasks used in build.xml to perform if/else logic
- required for executing build.xml

* cargo
- ant tasks and API to start/stop container when running canoo web tests
- required for running "test-jsp" and "test-all" targets

* checkstyle
- ant task to create reports on code syntax
- required for running "checkstyle" target 

* clickstream
- servlet filter and listener to track click paths
- required for running web application

* dbunit
- ant tasks and API for loading the database into a known state for testing
- required for running "test-*" targets

* displaytag
- JSP tag library for sorting and paging of lists
- required for running web application

* dumbster
- fake smtp server used when running unit tests
- required for running tests that send e-mail

* hibernate
- default persistence framework
- required for running tests and web application

* jakarta-log4j
- logging framework for java applications
- not required, but controls logging when included

* struts
- default web framework
- required for running web tests and application

* jakarta-taglibs
- JSTL and request tag libraries
- required for running web application

* java2html
- ant task for generating HTML version of source code
- required for running documentation and release targets

* javamail
- standard java mail library to send e-mail messages
- required for running web application

* jmock
- dynamic mock framework for unit testing
- required for building and testing the services layer

* junit
- testing framework for unit and integration tests
- required for building and running tests

* log4j
- logging framework
- required for running tests and web application

* mysql-jdbc-driver
- mysql jdbc driver to allow talking to a mysql database
- required for running application with mysql

* pmd
- ant task for generating code efficiency reports
- required for running "pmd" target 

* postgresql-jdbc-driver
- postgresql jdbc driver to allow talking to a postgres database
- required for running application with postgres

* rename-packages
- ant task to rename packages and directories
- required for running "new" target

* servlet-api
- standard servlet api that all web frameworks depend on
- required for building and testing, but not running

* sitemesh
- page decoration framework
- required for running web application

* spring
- the glue that binds the layers together and simplifies J2EE
- required for building, testing and running

* struts-menu
- navigation system for UI
- required for running web application

* strutstest
- testing framework for struts actions
- required for building and testing struts actions

* urlrewrite
- servlet filter that provides functionality like apache's mod_rewrite
- required for running web application (configured but not used)

* velocity
- templating framework for structuring e-mails
- required for building, testing and sending e-mail

* webtest
- testing framework for driving the UI
- required for running "test-jsp" and "test-canoo" targets

* xdoclet
- code generation framework used for generating deployment descriptors
- required for generating hibernate mapping files, web.xml and struts-config.xml
