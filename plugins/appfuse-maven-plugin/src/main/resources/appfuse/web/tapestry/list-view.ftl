<#assign pojoNameLower = pojo.shortName.substring(0,1).toLowerCase()+pojo.shortName.substring(1)>
<html t:type="layout" title="message:${pojoNameLower}List.title"
          heading="message:${pojoNameLower}List.heading" menu="${pojo.shortName}Menu"
          xmlns:t="http://tapestry.apache.org/schema/tapestry_5_3.xsd" xmlns:p="tapestry:parameter">

<div class="span10">
    <div id="search">
    <form t:type="form" method="get" t:id="searchForm" autofocus="false" class="form-search">
        <t:textfield size="20" name="q" t:id="q" placeholder="Enter search terms..." class="input-medium search-query"/>
        <input t:type="submit" value="${'$'}{message:button.search}" class="btn"/>
    </form>
    </div>

    <a t:type="eventlink" t:id="add" id="add">
        <input type="button" style="margin-right: 5px" class="btn" value="${'$'}{message:button.add}"/>
    </a>
    <a t:type="eventlink" t:id="done" id="done">
        <input type="button" class="btn" value="${'$'}{message:button.done}"/>
    </a>

    <t:grid source="${util.getPluralForWord(pojoNameLower)}" row="${pojoNameLower}" id="${pojoNameLower}List" class="table">
        <p:${pojo.identifierProperty.name}cell>
            <a t:type="actionlink" t:id="edit" context="${pojoNameLower}.${pojo.identifierProperty.name}" id="${pojoNameLower}-${'$'}{${pojoNameLower}.${pojo.identifierProperty.name}}">
                ${'$'}{${pojoNameLower}.${pojo.identifierProperty.name}}
            </a>
        </p:${pojo.identifierProperty.name}cell>
    </t:grid>
</div>
</html>