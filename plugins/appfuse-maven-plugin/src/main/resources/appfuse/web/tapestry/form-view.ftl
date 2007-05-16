<#assign pojoNameLower = pojo.shortName.substring(0,1).toLowerCase()+pojo.shortName.substring(1)>
<head>
    <title><span key="${pojoNameLower}Detail.title"/></title>
    <meta name="heading" jwcid="@Any" content="message:${pojoNameLower}Detail.heading"/>
</head>

<body jwcid="@Body">

<span jwcid="@ShowValidationError" delegate="ognl:delegate"/>
<span jwcid="@ShowMessage"/>

<form jwcid="${pojoNameLower}Form" id="${pojoNameLower}Form">
<#rt/>
<#foreach field in pojo.getAllPropertiesIterator()>
<#if field.equals(pojo.identifierProperty)>
    <#assign idFieldName = field.name>
    <#if field.value.identifierGeneratorStrategy == "assigned">
<ul>
    <li>
        <label class="desc" jwcid="@FieldLabel" field="component:${field.name}Field"/>
        <input class="text medium" jwcid="${field.name}Field" type="text" id="${field.name}"/>
    </li>
    <#else>
        <#lt/><input type="hidden" jwcid="@Hidden" value="ognl:${pojoNameLower}.${field.name}"/>

<ul>
    </#if>
<#elseif !c2h.isCollection(field) && !c2h.isManyToOne(field)>

    <li>
<#foreach column in field.getColumnIterator()>
    <#if field.value.typeName == "java.util.Date">
        <label jwcid="@Label" class="desc" for="${field.name}" key="${pojoNameLower}.${field.name}"/>
        <input jwcid="@DatePicker" type="text" size="11" value="ognl:${pojoNameLower}.${field.name}" id="${field.name}" translator="translator:date,pattern=MM/dd/yyyy"/> <!-- todo: figure out how to change this per-locale -->
    <#elseif field.value.typeName == "boolean" || field.value.typeName == "java.lang.Boolean">
        <label jwcid="@Label" class="desc" for="${field.name}" key="${pojoNameLower}.${field.name}"/>
        <input class="checkbox" type="checkbox" jwcid="@Checkbox" value="ognl:${pojoNameLower}.${field.name}" id="${field.name}"/>
    <#else>
        <label class="desc" jwcid="@FieldLabel" field="component:${field.name}Field"/>
        <input class="text medium" jwcid="${field.name}Field" type="text" id="${field.name}"/>
    </#if>
</#foreach>
<#elseif c2h.isManyToOne(field)>
    <#foreach column in field.getColumnIterator()>
            <#lt/>    <!-- todo: change this to read the identifier field from the other pojo -->
            <#lt/>    <select jwcid="${field.name}Field" size="1" class="select">
                    <option></option>
                </select>
    </#foreach>
    </li>
</#if>
</#foreach>

    <li class="buttonBar button">
        <input type="submit" class="button" jwcid="@Submit" value="message:button.save" id="save" action="listener:save"/>
      <span jwcid="@If" condition="ognl:${pojoNameLower}.${pojo.identifierProperty.name} != null">
        <input type="submit" class="button" jwcid="@Submit" value="message:button.delete" id="delete" action="listener:delete"
            onclick="form.onsubmit = null; return confirmDelete('${pojo.shortName}')"/>
      </span>
        <input type="submit" class="button" jwcid="@Submit" value="message:button.cancel" id="cancel" action="listener:cancel"
            onclick="form.onsubmit = null"/>
    </li>
</ul>
</form>

<script type="text/javascript">
    Form.focusFirstElement($("${pojoNameLower}Form"));
</script>

</body>