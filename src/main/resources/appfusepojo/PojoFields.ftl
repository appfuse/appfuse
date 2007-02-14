<#foreach field in pojo.getAllPropertiesIterator()>
<#if pojo.getMetaAttribAsBool(field, "gen-property", true)>
    <#if pojo.hasMetaAttribute(field, "field-description")>
    /**${pojo.getFieldJavaDoc(field, 0)} */
    </#if>
    ${pojo.getFieldModifiers(field)} ${pojo.getJavaTypeName(field, jdk5)} ${field.name}<#rt/>
    <#if pojo.hasFieldInitializor(field, jdk5)><#rt/>
        = ${pojo.getFieldInitialization(field, jdk5)}<#rt/>
    </#if>;
</#if>
</#foreach>
