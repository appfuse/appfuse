<#assign pojoNameLower = pojo.shortName.substring(0,1).toLowerCase()+pojo.shortName.substring(1)>
<!--${pojo.shortName}Manager-START-->
    <bean id="${pojoNameLower}Manager" class="${basepackage}.service.impl.${pojo.shortName}ManagerImpl">
        <constructor-arg ref="${pojoNameLower}Dao"/>
    </bean>
    <!--${pojo.shortName}Manager-END-->

    <!-- Add new Managers here -->