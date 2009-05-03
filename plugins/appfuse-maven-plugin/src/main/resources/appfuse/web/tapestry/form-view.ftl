<#assign pojoNameLower = pojo.shortName.substring(0,1).toLowerCase()+pojo.shortName.substring(1)>
<t:layout title="message:${pojoNameLower}Detail.title"
          heading="message:${pojoNameLower}Detail.heading" menu="literal:${pojo.shortName}Menu"
          xmlns:t="http://tapestry.apache.org/schema/tapestry_5_0_0.xsd">

    <t:messagebanner t:id="message" type="type"/>

    <form t:id="${pojoNameLower}Form" clientValidation="true">
        <t:errors/>

        <div class="t-beaneditor">
            <t:beaneditor t:id="${pojoNameLower}" object="${pojoNameLower}" exclude="${pojo.identifierProperty.name}"/>
            
            <div class="t-beaneditor-row" style="text-align: center">
                <input t:type="submit" t:id="save" id="save" value="message:button.save"/>
                <input t:type="submit" t:id="delete" id="delete" value="message:button.delete"
                       onclick="return confirmDelete('${pojo.shortName}')"/>
                <input t:type="submit" t:id="cancel" id="cancel" value="message:button.cancel"/>
            </div>
        </div>
    </form>

    <script type="text/javascript">
        Form.focusFirstElement($("${pojoNameLower}Form"));
    </script>

</t:layout>