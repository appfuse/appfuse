<#-- This template is not used anymore (APF-798), but retained in case we do want to create definitions by default in the future -->
<#assign pojoNameLower = pojo.shortName.substring(0,1).toLowerCase()+pojo.shortName.substring(1)>
<!--${pojo.shortName}Action-START-->
    <bean id="${pojoNameLower}Action" class="${basepackage}.webapp.action.${pojo.shortName}Action" scope="prototype">
        <property name="${pojoNameLower}Manager" ref="${pojoNameLower}Manager"/>
    </bean>
    <!--${pojo.shortName}Action-END-->

    <!-- Add new Actions here -->