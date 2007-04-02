<#foreach field in pojo.getAllPropertiesIterator()><#rt/>
<#if pojo.getMetaAttribAsBool(field, "gen-property", true)><#rt/>
    ${pojo.getFieldModifiers(field)} ${pojo.getJavaTypeName(field, jdk5)} ${field.name}<#if pojo.hasFieldInitializor(field, jdk5)> = ${pojo.getFieldInitialization(field, jdk5)}</#if>;
</#if><#rt/>
</#foreach><#rt/>