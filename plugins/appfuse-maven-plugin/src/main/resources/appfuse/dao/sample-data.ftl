    <!--${pojo.shortName}-START-->
    <table name="${clazz.table.name}">
<#foreach field in pojo.getAllPropertiesIterator()>
<#if !c2h.isCollection(field) && !c2h.isManyToOne(field) && !c2j.isComponent(field)>
    <#foreach column in field.getColumnIterator()>
        <column>${column.name}</column>
    </#foreach>
</#if>
</#foreach>
<#assign rows = 1 .. 3>
<#foreach num in rows>
    <row>
<#foreach field in pojo.getAllPropertiesIterator()>
<#if !c2h.isCollection(field) && !c2h.isManyToOne(field) && !c2j.isComponent(field)>
    <#foreach column in field.getColumnIterator()>
        <value description="${column.name}"><#rt/>
        <#if field.equals(pojo.identifierProperty)>
            <#lt/>${num}<#rt/>
        <#elseif field.equals(pojo.versionProperty)>
            1<#rt/>
        <#else>
            <#lt/>${data.getTestValueForDbUnit(field.value.typeName)}<#rt/>
        </#if>
        <#lt/></value>
    </#foreach>
</#if>
</#foreach>
    </row>
</#foreach>
    </table>
    <!--${pojo.shortName}-END-->
</dataset>