    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ${pojo.getDeclarationName()} pojo = (${pojo.getDeclarationName()}) o;

    <#list pojo.getAllPropertiesIterator() as property><#rt/>
        <#if !property.equals(clazz.identifierProperty)><#rt/>
        <#if pojo.getJavaTypeName(property, jdk5) == "boolean"><#rt/>
        if (${property.getName()} != pojo.${property.getName()}) return false;
        <#elseif !c2h.isCollection(property)><#rt/>
        if (${property.getName()} != null ? !${property.getName()}.equals(pojo.${property.getName()}) : pojo.${property.getName()} != null) return false;
        </#if><#rt/>
        </#if><#rt/>
    </#list><#rt/>

        return true;
    }

    public int hashCode() {
        int result = 0;
    <#list pojo.getAllPropertiesIterator() as property><#rt/>
        <#if !property.equals(clazz.identifierProperty)><#rt/>
    <#if property_index == 1><#rt/>
        <#if pojo.getJavaTypeName(property, jdk5) == "boolean"><#rt/>
        result = (${property.getName()} ? 1 : 0);
        <#elseif !c2h.isCollection(property)><#rt/>
        result = (${property.getName()} != null ? ${property.getName()}.hashCode() : 0);
        </#if><#rt/>
    <#else><#rt/>
        <#if pojo.getJavaTypeName(property, jdk5) == "boolean"><#rt/>
        result = 31 * result + (${property.getName()} ? 1 : 0);
        <#elseif !c2h.isCollection(property)><#rt/>
        result = 31 * result + (${property.getName()} != null ? ${property.getName()}.hashCode() : 0);
        </#if><#rt/>
    </#if><#rt/>
        </#if><#rt/>
    </#list><#rt/>

        return result;
    }