<#assign pojoNameLower = pojo.shortName.substring(0,1).toLowerCase()+pojo.shortName.substring(1)>
        <!--${pojo.shortName}-START-->
        <form name="${pojoNameLower}">
    <#foreach field in pojo.getAllPropertiesIterator()>
        <#foreach column in field.getColumnIterator()>
            <#if !field.equals(pojo.identifierProperty) && !column.nullable && !c2h.isCollection(field) && !c2h.isManyToOne(field) && !c2j.isComponent(field) && !column.nullable>
            <field property="${field.name}" depends="required">
                <arg key="${pojoNameLower}.${field.name}"/>
            </field>
            </#if>
        </#foreach>
    </#foreach>
        </form>
        <!--${pojo.shortName}-END-->
    </formset>