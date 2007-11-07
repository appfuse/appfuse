
    public String toString() {
        StringBuffer sb = new StringBuffer(getClass().getSimpleName());

        sb.append(" [");
<#list pojo.getAllPropertiesIterator() as property>        <#if !c2h.isCollection(property)>sb.append("${property.getName()}").append("='").append(${pojo.getGetterSignature(property)}()).append("'<#if property_has_next>, </#if>");</#if>
</#list>        sb.append("]");
      
        return sb.toString();
    }