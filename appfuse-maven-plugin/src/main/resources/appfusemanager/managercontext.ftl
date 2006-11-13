<#if pojo.isComponent()>
 <#else>
<!--${pojo.getDeclarationName()}Manager-START-->
    <bean id="${assist.convertName(pojo.getDeclarationName())}Manager" parent="${transactionproxyname}">
        <property name="target">
            <bean
                class="${managerimplpackagename}.${pojo.getDeclarationName()}ManagerImpl"
                autowire="byName" />
        </property>
    </bean>
 <!--${pojo.importType(pojo.getDeclarationName())}Manager-END-->
</#if>