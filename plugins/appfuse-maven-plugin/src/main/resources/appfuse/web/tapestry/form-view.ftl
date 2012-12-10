<#assign pojoNameLower = pojo.shortName.substring(0,1).toLowerCase()+pojo.shortName.substring(1)>
<html t:type="layout" title="message:${pojoNameLower}Detail.title"
          heading="message:${pojoNameLower}Detail.heading" menu="${pojo.shortName}Menu"
          xmlns:t="http://tapestry.apache.org/schema/tapestry_5_3.xsd">

<div class="span2">
    <h2>${'$'}{message:${pojoNameLower}Detail.heading}</h2>
    ${'$'}{message:${pojoNameLower}Detail.message}
</div>

<div class="span7">
    <form t:id="${pojoNameLower}Form" class="well form-horizontal" clientValidation="true" >

        <div class="t-beaneditor">
            <t:beaneditor t:id="${pojoNameLower}" object="${pojoNameLower}" exclude="${pojo.identifierProperty.name}"/>
            
            <div class="t-beaneditor-row form-actions" style="text-align: center">
                <button type="submit" id="save" class="btn btn-primary"><i class="icon-ok icon-white"></i> ${'$'}{message:button.save}</button>
                <t:if test="person.id">
                <button t:type="eventlink" t:id="delete" id="delete" class="btn btn-danger" onclick="return confirmDelete('${pojo.shortName}')">
                    <i class="icon-trash"></i> ${'$'}{message:button.delete}
                </button>
                </t:if>
                <button t:type="eventlink" t:id="cancel" id="cancel" class="btn"><i class="icon-remove"></i> ${'$'}{message:button.cancel}</button>
            </div>
        </div>
    </form>
</div>
</html>