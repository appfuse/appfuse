iBATIS Integration Instructions
================================================================================

More information on my integration of iBATIS (http://ibatis.com) can be found
at:

http://raibledesigns.com/page/rd?anchor=appfuse_refactorings_part_iv_replacing

To install iBATIS as a persistence framework, you need to navigate to this
directory from the command line.  Then you can execute any of the following
targets with Ant. It might not be the most robust installer (it'll create
duplicates if run twice), but it seems to work good enough.

                install: installs iBatis into AppFuse
              uninstall: uninstalls iBatis from AppFuse
    uninstall-hibernate: uninstalls Hibernate from AppFuse

                   help: Print this help text.

----
All of these targets simply parse lib.properties, build.xml and properties.xml
to add/delete iBATIS stuff or delete Hibernate stuff. They also install/remove
JARs and source .java and .sql files. If you're going to run this installer, I
recommend running "ant install uninstall-hibernate". Of course, you can also
simply "install" it and then change the dao.type in properties.xml. This will
allow you to use both Hibernate and iBATIS DAOs side-by-side.

To use both Hibernate and iBATIS in an application, you could create an
applicationContext-hibatis.xml file in src/dao/org/appfuse/persistence and
change the dao.type to be hibatis (like that nickname ;-). In this file, you'd
have to then define your transactionManager and sqlMap/sessionFactory. I
tested this and it works pretty slick. Below is the
applicationContext-hibatis.xml file I used.

================================================================================
<!-- Hibernate SessionFactory -->
<bean id="sessionFactory"
  class="org.springframework.orm.hibernate3.LocalSessionFactoryBean">
  <property name="dataSource"><ref bean="dataSource"/></property>
  <property name="mappingResources">
    <list>
      <value>org/appfuse/model/Role.hbm.xml</value>
      <value>org/appfuse/model/User.hbm.xml</value>
      <value>org/appfuse/model/UserCookie.hbm.xml</value>
      <value>org/appfuse/model/UserRole.hbm.xml</value>
    </list>
  </property>
  <property name="hibernateProperties">
    <props>
      <prop key="hibernate.dialect">@HIBERNATE-DIALECT@</prop>
    </props>
  </property>
</bean>

<!-- SqlMap setup for iBATIS Database Layer -->
<bean id="sqlMapClient" class="org.springframework.orm.ibatis.SqlMapClientFactoryBean">
  <property name="configLocation">
    <value>classpath:/org/appfuse/persistence/ibatis/sql-map-config.xml</value>
  </property>
</bean>

<!-- Transaction manager for a single JDBC DataSource -->
<bean id="transactionManager"
  class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
  <property name="dataSource"><ref bean="dataSource"/></property>
</bean>

<!-- LookupDAO: iBatis implementation -->
<bean id="lookupDAO" class="org.appfuse.persistence.ibatis.LookupDAOiBatis">
  <property name="dataSource"><ref bean="dataSource"/></property>
  <property name="sqlMapClient"><ref local="sqlMapClient"/></property>
</bean>

<!-- UserDAO: Hibernate implementation -->
<bean id="userDAO" class="org.appfuse.persistence.hibernate.UserDAOHibernate">
  <property name="sessionFactory"><ref bean="sessionFactory"/></property>
</bean>

================================================================================

Some things I noticed in the process of developing this:

Running "ant clean test-dao" with iBATIS (28 seconds) is a bit faster than 
Hibernate (33 seconds). I'm sure if I optimized Hibernate, I could make these 
numbers equal.

The iBATIS install is about 500K, whereas Hibernate's JARs are around 2 MB. So 
using iBATIS will get you a slightly faster and smaller AppFuse application, 
but it's a bit harder to manipulate the database on the fly. There's no way of 
generating the tables/columns with iBATIS. Instead it uses a table creation 
script - so if you add new persistent objects, you'll have to manually edit 
the table creation SQL.

Hibernate is still the right decision for me, but it's cool that iBATIS is an 
option. Even cooler is the fact that you can mix and match Hibernate and 
iBATIS DAOs.
