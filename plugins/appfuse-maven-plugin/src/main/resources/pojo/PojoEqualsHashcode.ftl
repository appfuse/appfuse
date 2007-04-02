    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ${pojo.getDeclarationName()} pojo = (${pojo.getDeclarationName()}) o;

    <#list pojo.getAllPropertiesIterator() as property><#rt/>
        <#if !property.equals(clazz.identifierProperty)><#rt/>
        if (${property.getName()} != null ? !${property.getName()}.equals(pojo.${property.getName()}) : pojo.${property.getName()} != null) return false;
        </#if><#rt/>
    </#list><#rt/>

        return true;
    }

    public int hashCode() {
        int result;
    <#list pojo.getAllPropertiesIterator() as property><#rt/>
        <#if !property.equals(clazz.identifierProperty)><#rt/>
    <#if property_index == 1><#rt/>
        result = (${property.getName()} != null ? ${property.getName()}.hashCode() : 0);
    <#else><#rt/>
        result = 31 * result + (${property.getName()} != null ? ${property.getName()}.hashCode() : 0);
    </#if><#rt/>
        </#if><#rt/>
    </#list><#rt/>

        return result;
    }