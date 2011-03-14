<#assign pojoNameLower = pojo.shortName.substring(0,1).toLowerCase()+pojo.shortName.substring(1)>
<!--${pojo.shortName}-beans-START-->
    <managed-bean>
        <managed-bean-name>${pojoNameLower}List</managed-bean-name>
        <managed-bean-class>${basepackage}.webapp.action.${pojo.shortName}List</managed-bean-class>
        <managed-bean-scope>session</managed-bean-scope>
        <managed-property>
            <property-name>${pojoNameLower}Manager</property-name>
            <value>${'#'}{${pojoNameLower}Manager}</value>
        </managed-property>
    </managed-bean>

    <managed-bean>
        <managed-bean-name>${pojoNameLower}Form</managed-bean-name>
        <managed-bean-class>${basepackage}.webapp.action.${pojo.shortName}Form</managed-bean-class>
        <managed-bean-scope>request</managed-bean-scope>
        <managed-property>
            <property-name>${pojo.identifierProperty.name}</property-name>
            <value>${'#'}{param.${pojo.identifierProperty.name}}</value>
        </managed-property>
        <managed-property>
            <property-name>${pojoNameLower}Manager</property-name>
            <value>${'#'}{${pojoNameLower}Manager}</value>
        </managed-property>
    </managed-bean>
    <!--${pojo.shortName}-beans-END-->

    <!-- Add additional beans here -->