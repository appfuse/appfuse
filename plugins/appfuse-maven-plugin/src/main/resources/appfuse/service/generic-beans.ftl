<!--${pojo.shortName}Manager-START-->
    <bean id="${pojo.shortName.toLowerCase()}Manager" class="${appfusepackage}.service.impl.GenericManagerImpl">
        <constructor-arg>
            <#if daoframework == "hibernate">
            <bean class="${appfusepackage}.dao.hibernate.GenericDaoHibernate">
                <constructor-arg value="${pojo.packageName}.${pojo.shortName}"/>
                <property name="sessionFactory" ref="sessionFactory"/>
            </bean>
            <#elseif daoframework == "ibatis">
            <bean class="${appfusepackage}.dao.ibatis.GenericDaoiBatis">
                <constructor-arg value="${pojo.packageName}.${pojo.shortName}"/>
                <property name="dataSource" ref="dataSource"/>
                <property name="sqlMapClient" ref="sqlMapClient"/>
            </bean>
            <#elseif daoframework == "jpa-hibernate">
            <bean class="${appfusepackage}.dao.jpa.GenericDaoJpa">
                <constructor-arg value="${pojo.packageName}.${pojo.shortName}"/>
            </bean>
            </#if>
        </constructor-arg>
    </bean>
    <!--${pojo.shortName}Manager-END-->

    <!-- Add new Managers here -->