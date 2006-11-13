<#if pojo.isComponent()>
 <#else>
<!--${pojo.getDeclarationName()}Dao-START-->
<bean id="${assist.convertName(pojo.getDeclarationName())}Dao" 
      class="${hibernatedaopackagename}.${pojo.getDeclarationName()}HibernateDao"
      autowire="byName"  >
       <property name="sessionFactory">
            <ref local="${sessionfactoryname}" />
        </property>
</bean>
<!--${pojo.getDeclarationName()}Dao-END-->
</#if>