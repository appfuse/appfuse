<#assign pojoNameLower = pojo.shortName.substring(0,1).toLowerCase()+pojo.shortName.substring(1)>
<!--${pojo.shortName}Dao-START-->
<#if daoframework == "hibernate">
    <bean id="${pojoNameLower}Dao" class="${basepackage}.dao.hibernate.${pojo.shortName}DaoHibernate"/>
<#elseif daoframework == "ibatis">
    <bean id="${pojoNameLower}Dao" class="${basepackage}.dao.ibatis.${pojo.shortName}DaoiBatis"/>
<#elseif daoframework == "jpa">
    <bean id="${pojoNameLower}Dao" class="${basepackage}.dao.jpa.${pojo.shortName}DaoJpa"/>
</#if>
    <!--${pojo.shortName}Dao-END-->

    <!-- Add new DAOs here -->