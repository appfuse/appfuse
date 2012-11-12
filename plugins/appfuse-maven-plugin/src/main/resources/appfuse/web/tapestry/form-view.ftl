<#assign pojoNameLower = pojo.shortName.substring(0,1).toLowerCase()+pojo.shortName.substring(1)>
<html t:type="layout" title="message:${pojoNameLower}Detail.title"
          heading="message:${pojoNameLower}Detail.heading" menu="${pojo.shortName}Menu"
          xmlns:t="http://tapestry.apache.org/schema/tapestry_5_3.xsd">

<div class="span2">
    <h2>${'$'}{message:${pojoNameLower}Detail.heading}</h2>
    ${'$'}{message:${pojoNameLower}Detail.message}
</div>

<div class="span7">
    <form t:type="form" t:id="${pojoNameLower}Form" clientValidation="true" class="well form-horizontal">
        <t:errors/>

        <div class="t-beaneditor">
            <t:beaneditor t:id="${pojoNameLower}" object="${pojoNameLower}" exclude="${pojo.identifierProperty.name}"/>
            
            <div class="t-beaneditor-row form-actions" style="text-align: center">
                <input t:type="submit" t:id="save" id="save" value="message:button.save" class="btn btn-primary"/>
                <input t:type="submit" t:id="delete" id="delete" value="message:button.delete" class="btn btn-warning"
                       onclick="return confirmDelete('${pojo.shortName}')"/>
                <input t:type="submit" t:id="cancel" id="cancel" value="message:button.cancel" class="btn"/>
            </div>
        </div>
    </form>
</div>
</html>