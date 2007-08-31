<#assign pojoNameLower = pojo.shortName.substring(0,1).toLowerCase()+pojo.shortName.substring(1)>
<!--${pojo.shortName}-START-->
    <bean id="${pojoNameLower}Controller" class="${basepackage}.webapp.controller.${pojo.shortName}Controller">
        <property name="${pojoNameLower}Manager" ref="${pojoNameLower}Manager"/>
    </bean>

    <bean id="${pojoNameLower}FormController" class="${basepackage}.webapp.controller.${pojo.shortName}FormController">
        <property name="validator" ref="beanValidator"/>
        <property name="successView" value="redirect:${util.getPluralForWord(pojoNameLower)}.html"/>
        <property name="${pojoNameLower}Manager" ref="${pojoNameLower}Manager"/>
    </bean>
    <!--${pojo.shortName}-END-->

    <!-- Add additional controller beans here -->