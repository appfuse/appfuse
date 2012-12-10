<#assign pojoNameLower = pojo.shortName.substring(0,1).toLowerCase()+pojo.shortName.substring(1)>
<html t:type="layout" title="message:${pojoNameLower}List.title"
          heading="message:${pojoNameLower}List.heading" menu="${pojo.shortName}Menu"
          xmlns:t="http://tapestry.apache.org/schema/tapestry_5_3.xsd" xmlns:p="tapestry:parameter">

<t:if test="errorMessage">
    <div class="alert alert-error fade in">
        <a href="#" data-dismiss="alert" class="close">&times;</a>
        ${'$'}{errorMessage}
    </div>
</t:if>

<div class="span10">
    <h2>${'$'}{message:${pojoNameLower}List.heading}</h2>

    <form t:type="form" method="get" t:id="searchForm" autofocus="false" class="form-search">
    <div id="search" class="input-append">
        <t:textfield size="20" name="q" t:id="q" placeholder="${'$'}{message:search.enterTerms}" class="input-medium search-query"/>
        <button type="submit" class="btn"><i class="icon-search"></i> ${'$'}{message:button.search}</button>
    </div>
    </form>

    <div id="actions" class="form-actions">
        <a t:type="eventlink" event="add" id="add" class="btn btn-primary"><i class="icon-plus icon-white"></i> ${'$'}{message:button.add}</a>
        <a t:type="eventlink" event="done" id="done" class="btn"><i class="icon-ok"></i> ${'$'}{message:button.done}</a>
    </div>

    <t:grid source="${util.getPluralForWord(pojoNameLower)}" row="${pojoNameLower}" id="${pojoNameLower}List" class="table table-condensed table-striped table-hover">
        <p:${pojo.identifierProperty.name}cell>
            <a t:type="actionlink" t:id="edit" context="${pojoNameLower}.${pojo.identifierProperty.name}" id="${pojoNameLower}-${'$'}{${pojoNameLower}.${pojo.identifierProperty.name}}">
                ${'$'}{${pojoNameLower}.${pojo.identifierProperty.name}}
            </a>
        </p:${pojo.identifierProperty.name}cell>
    </t:grid>
</div>
</html>