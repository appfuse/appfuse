<#assign pojoNameLower = pojo.shortName.substring(0,1).toLowerCase()+pojo.shortName.substring(1)>
<!DOCTYPE validators PUBLIC "-//Apache Struts//XWork Validator 1.0.3//EN"
        "http://struts.apache.org/dtds/xwork-validator-1.0.3.dtd">
<validators>
<#foreach field in pojo.getAllPropertiesIterator()>
    <#foreach column in field.getColumnIterator()>
        <#if !field.equals(pojo.identifierProperty) && !column.nullable && !c2h.isCollection(field) && !c2h.isManyToOne(field) && !c2j.isComponent(field) && !column.nullable>
            <#assign type = field.value.typeName>
            <#if type != "boolean" && type != "java.lang.Boolean">
            <#lt/>    <field name="${pojoNameLower}.${field.name}">
                <#if type == "java.lang.String">
                    <#lt/>        <field-validator type="requiredstring">
                <#else>
                    <#lt/>        <field-validator type="required">
                </#if>
            <#lt/>            <message key="errors.required"/>
            <#lt/>        </field-validator>
            <#lt/>    </field>
            </#if>
        </#if>
    </#foreach>
</#foreach>
</validators> 