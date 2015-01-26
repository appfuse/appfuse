<#assign pojoNameLower = pojo.shortName.substring(0,1).toLowerCase()+pojo.shortName.substring(1)>
<html xmlns:wicket="http://wicket.apache.org">
<head>
    <title><wicket:message key="${pojoNameLower}List.title"/></title>
</head>
<body>
    <div wicket:id="feedback" class="alert alert-success alert-dismissable"></div>

    <h2><wicket:message key="${pojoNameLower}List.title"/></h2>

    <form wicket:id="form">
        <button wicket:id="add-${pojoNameLower}" type="submit" class="btn btn-primary" style="float: right; margin-top: -10px">
            <wicket:message key="button.add"/></button>
    </form>

    <span wicket:id="navigator"/>

    <table class="table table-condensed table-striped table-hover" id="${pojoNameLower}List">
        <thead>
        <tr>
        <#foreach field in pojo.getAllPropertiesIterator()>
            <th wicket:id="orderBy${pojo.getPropertyName(field)}"><wicket:message key="${pojoNameLower}.${field.name}"/></th>
        </#foreach>
        </tr>
        </thead>
        <tbody>
        <tr wicket:id="${util.getPluralForWord(pojoNameLower)}">
    <#foreach field in pojo.getAllPropertiesIterator()>
        <#if field.equals(pojo.identifierProperty)>
            <td><a href="#" wicket:id="edit-link"><span wicket:id="${pojoNameLower}.${pojo.identifierProperty.name}"></span></a></td>
        <#elseif !c2h.isCollection(field) && !c2h.isManyToOne(field) && !c2j.isComponent(field)>
            <td><span wicket:id="${pojoNameLower}.${field.name}"></span></td>
        </#if>
    </#foreach>
        </tr>
        </tbody>
    </table>
</body>
</html>
