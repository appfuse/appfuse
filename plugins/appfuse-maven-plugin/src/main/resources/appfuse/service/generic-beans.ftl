<!--${pojo.shortName}Manager-START-->
    <bean id="${pojo.shortName.toLowerCase()}Manager" class="org.appfuse.service.impl.GenericManagerImpl">      
        <constructor-arg>
            <#if daoframework == "hibernate">
                <bean  class="org.appfuse.dao.hibernate.GenericDaoHibernate">
                    <constructor-arg value="${pojo.packageName}.${pojo.shortName}"/>
                    <property name="sessionFactory" ref="sessionFactory"/>
                </bean>
            <#elseif daoframework == "ibatis">
                <bean class="org.appfuse.dao.ibatis.GenericDaoiBatis">
                    <constructor-arg value="${pojo.packageName}.${pojo.shortName}"/>
                    <property name="dataSource" ref="dataSource"/>
                    <property name="sqlMapClient" ref="sqlMapClient"/>
                </bean>
            <#elseif daoframework == "jpa-hibernate">
                <bean class="org.appfuse.dao.jpa.GenericDaoJpa">
                    <constructor-arg value="${pojo.packageName}.${pojo.shortName}"/>
                </bean>
            </#if>
        </constructor-arg>
    </bean>
    <!--${pojo.shortName}Manager-END-->

    <!-- Add new Managers here -->