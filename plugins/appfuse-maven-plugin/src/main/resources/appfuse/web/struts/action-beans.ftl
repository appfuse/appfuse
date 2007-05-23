<#assign pojoNameLower = pojo.shortName.substring(0,1).toLowerCase()+pojo.shortName.substring(1)>
<!--${pojo.shortName}Action-START-->
    <bean id="${pojoNameLower}Action" class="${basepackage}.webapp.action.${pojo.shortName}Action" scope="prototype">
        <property name="${pojoNameLower}Manager" ref="${pojoNameLower}Manager"/>
    </bean>
    <!--${pojo.shortName}Action-END-->

    <#if webframework == "struts"><!-- Add new Actions here --></#if>