    <bean id="${pojo.shortName.toLowerCase()}Controller" class="${basepackage}.webapp.controller.${pojo.shortName}Controller">
        <property name="${pojo.shortName.toLowerCase()}Manager" ref="${pojo.shortName.toLowerCase()}Manager"/>
    </bean>

    <bean id="${pojo.shortName.toLowerCase()}FormController" class="${basepackage}.webapp.controller.${pojo.shortName}FormController">
        <property name="validator" ref="beanValidator"/>
        <property name="successView" value="redirect:${pojo.shortName.toLowerCase()}s.html"/>
        <property name="${pojo.shortName.toLowerCase()}Manager" ref="${pojo.shortName.toLowerCase()}Manager"/>
    </bean>