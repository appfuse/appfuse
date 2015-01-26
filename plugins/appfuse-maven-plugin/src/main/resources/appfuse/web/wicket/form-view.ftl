<#assign pojoNameLower = pojo.shortName.substring(0,1).toLowerCase()+pojo.shortName.substring(1)>
<html xmlns:wicket="http://wicket.apache.org">
<head>
    <title><wicket:message key="${pojoNameLower}Detail.title"/></title>
</head>
<body>
    <div class="col-sm-3">
        <h2><wicket:message key="${pojoNameLower}Detail.heading"/></h2>
        <wicket:message key="${pojoNameLower}Detail.message"/>
    </div>
    <div class="col-sm-6">
        <div wicket:id="feedback" class="alert alert-dismissable"></div>

        <form wicket:id="${pojoNameLower}-form" id="${pojoNameLower}Form" class="well">
    <#foreach field in pojo.getAllPropertiesIterator()>
        <#assign isDate = false>
        <#assign isBoolean = false>
        <#if field.equals(pojo.identifierProperty)>
            <#assign idFieldName = field.name>
            <#if field.value.identifierGeneratorStrategy == "assigned">
                <#lt/>
                <div class="form-group">
                    <label for="${field.name}" class="control-label"><wicket:message key="${pojoNameLower}.${field.name}">${data.getFieldDescription(field.name)}</wicket:message>:</label>
                    <input type="text" wicket:id="${field.name}" id="${field.name}" class="form-control"/>
                    <div wicket:id="${field.name}-feedback" class="help-block">[${field.name} errors]</div>
                </div>
            </div>
            </#if>
        <#elseif !c2h.isCollection(field) && !c2h.isManyToOne(field) && !c2j.isComponent(field)>
            <#foreach column in field.getColumnIterator()>
                <#if field.value.typeName == "java.util.Date" || field.value.typeName == "date">
                    <#assign isDate = true>
                </#if>
                <#if field.value.typeName == "boolean" || field.value.typeName == "java.lang.Boolean">
                    <#assign isBoolean = true>
                </#if>
                <#if isBoolean>
                    <#lt/>            <div class="checkbox">
                    <#lt/>                <label for="${field.name}">
                    <#lt/>                    <input type="checkbox" id="${field.name}" wicket:id="${field.name}"/>
                    <#lt/>                    <wicket:message key="${pojoNameLower}.${field.name}">${data.getFieldDescription(field.name)}</wicket:message>
                    <#lt/>                </label>
                    <#lt/>                <div wicket:id="${field.name}-feedback" class="help-block">[${field.name} errors]</div>
                    <#lt/>            </div>
                <#else>
                    <#lt/>            <div class="form-group">
                    <#lt/>                <label for="${field.name}" class="control-label"><wicket:message key="${pojoNameLower}.${field.name}">${data.getFieldDescription(field.name)}</wicket:message>:</label>
                    <#lt/>                <input type="text" wicket:id="${field.name}" id="${field.name}" class="form-control"/>
                    <#lt/>                <div wicket:id="${field.name}-feedback" class="help-block">[${field.name} errors]</div>
                    <#lt/>            </div>
                </#if>
            </#foreach>
        </#if>
    </#foreach>
            <div class="form-group">
                <button type="submit" class="btn btn-primary" id="save" wicket:id="save">
                    <i class="icon-ok icon-white"></i> <wicket:message key="button.save">Save</wicket:message>
                </button>

                <button type="submit" class="btn btn-danger" id="delete" wicket:id="delete">
                    <i class="icon-trash"></i> <wicket:message key="button.delete">Delete</wicket:message>
                </button>

                <a href="${util.getPluralForWord(pojoNameLower)}" class="btn btn-default" wicket:id="cancel">
                    <i class="icon-remove"></i> <wicket:message key="button.cancel">Cancel</wicket:message>
                </a>
            </div>
        </form>
    </div>
    <script type="text/javascript">
        $(document).ready(function() {
            $("input[type='text']:visible:enabled:first", document.forms['${pojoNameLower}Form']).focus();
        });
    </script>
</body>
</html>
