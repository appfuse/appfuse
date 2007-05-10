<!--${pojo.shortName}Action-START-->
    <bean id="${pojo.shortName.toLowerCase()}Action" class="${basepackage}.webapp.action.${pojo.shortName}Action" scope="prototype">
        <property name="${pojo.shortName.toLowerCase()}Manager" ref="${pojo.shortName.toLowerCase()}Manager"/>
    </bean>
    <!--${pojo.shortName}Action-END-->

    <#if webframework == "struts"><!-- Add new Actions here --></#if>