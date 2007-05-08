    <!--${pojo.shortName}Dao-START-->
    <#if daoframework == "hibernate">
        <bean id="${pojo.shortName.toLowerCase()}Dao" class="${basepackage}.dao.hibernate.${pojo.shortName}HibernateDao">
            <property name="sessionFactory" ref="sessionFactory"/>
        </bean>
    <#elseif daoframework == "ibatis">
        <bean id="${pojo.shortName.toLowerCase()}Dao" class="${basepackage}.dao.ibatis.${pojo.shortName}iBatisDao">
            <property name="dataSource" ref="dataSource"/>
            <property name="sqlMapClient" ref="sqlMapClient"/>
        </bean>
    <#elseif daoframework == "jpa-hibernate">
        <bean id="${pojo.shortName.toLowerCase()}Dao" class="${basepackage}.dao.jpa.${pojo.shortName}JpaDao"/>
    </#if>
    <!--${pojo.shortName}Dao-END-->

    <!-- Add new DAOs here -->