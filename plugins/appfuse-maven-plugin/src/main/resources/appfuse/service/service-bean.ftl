<!--${pojo.shortName}Manager-START-->
    <bean id="${pojo.shortName.toLowerCase()}Manager" class="${basepackage}.service.impl.${pojo.shortName}ManagerImpl">
        <constructor-arg ref="${pojo.shortName.toLowerCase()}Dao"/>
    </bean>
    <!--${pojo.shortName}Manager-END-->

    <!-- Add new Managers here -->