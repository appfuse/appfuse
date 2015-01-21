<#assign pojoNameLower = pojo.shortName.substring(0,1).toLowerCase()+pojo.shortName.substring(1)>
<html t:type="layout" title="message:${pojoNameLower}List.title"
          heading="message:${pojoNameLower}List.heading" menu="${pojo.shortName}Menu"
          xmlns:t="http://tapestry.apache.org/schema/tapestry_5_4.xsd" xmlns:p="tapestry:parameter">

<t:if test="errorMessage">
    <div class="alert alert-danger alert-dismissable">
        <a href="#" data-dismiss="alert" class="close">&times;</a>
        ${'$'}{errorMessage}
    </div>
</t:if>

<h2>${'$'}{message:${pojoNameLower}List.heading}</h2>

<form t:type="form" method="get" t:id="searchForm" autofocus="false" class="form-inline">
<div id="search" class="text-right">
    <span class="col-sm-9">
        <t:textfield size="20" name="q" t:id="q" placeholder="${'$'}{message:search.enterTerms}" class="form-control input-sm"/>
    </span>
    <button type="submit" class="btn btn-default btn-sm"><i class="icon-search"></i> ${'$'}{message:button.search}</button>
</div>
</form>

<p>${'$'}{message:${pojoNameLower}List.message}</p>

<div id="actions" class="btn-group">
    <a t:type="eventlink" event="add" id="add" class="btn btn-primary"><i class="icon-plus icon-white"></i> ${'$'}{message:button.add}</a>
    <a t:type="eventlink" event="done" id="done" class="btn btn-default"><i class="icon-ok"></i> ${'$'}{message:button.done}</a>
</div>

<t:grid source="${util.getPluralForWord(pojoNameLower)}" row="${pojoNameLower}" id="${pojoNameLower}List" class="table table-condensed table-striped table-hover">
    <p:${pojo.identifierProperty.name}cell>
        <a t:type="actionlink" t:id="edit" context="${pojoNameLower}.${pojo.identifierProperty.name}" id="${pojoNameLower}-${'$'}{${pojoNameLower}.${pojo.identifierProperty.name}}">
            ${'$'}{${pojoNameLower}.${pojo.identifierProperty.name}}
        </a>
    </p:${pojo.identifierProperty.name}cell>
</t:grid>
</html>
