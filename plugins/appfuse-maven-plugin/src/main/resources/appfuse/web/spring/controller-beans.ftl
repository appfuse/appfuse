<#assign pojoNameLower = pojo.shortName.substring(0,1).toLowerCase()+pojo.shortName.substring(1)>
<!--${pojo.shortName}-START-->
    <bean id="${pojoNameLower}Controller" class="${basepackage}.webapp.controller.${pojo.shortName}Controller"/>

    <bean id="${pojoNameLower}FormController" class="${basepackage}.webapp.controller.${pojo.shortName}FormController">
        <property name="validator" ref="beanValidator"/>
        <property name="successView" value="redirect:${util.getPluralForWord(pojoNameLower)}.html"/>
    </bean>
    <!--${pojo.shortName}-END-->

    <!-- Add additional controller beans here -->