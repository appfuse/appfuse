    <!--${pojo.shortName}Manager-START-->
    <bean id="${pojo.shortName.toLowerCase()}Manager" class="${basepackage}.service.impl.${pojo.shortName}Manager">
        <property name="${pojo.shortName.toLowerCase()}Dao" ref="${pojo.shortName.toLowerCase()}Dao"/>
    </bean>
    <!--${pojo.shortName}Manager-END-->