<#assign pojoNameLower = pojo.shortName.substring(0,1).toLowerCase()+pojo.shortName.substring(1)>
<t:layout title="message:${pojoNameLower}Detail.title"
          heading="message:${pojoNameLower}Detail.heading" menu="literal:${pojo.shortName}Menu"
          xmlns:t="http://tapestry.apache.org/schema/tapestry_5_0_0.xsd">

    <t:messagebanner t:id="message" type="type"/>
    <t:beaneditform object="${pojoNameLower}" id="${pojoNameLower}Form" exclude="${pojo.identifierProperty.name}"
                    clientValidation="true" submitLabel="message:button.save" add="buttons">
        <!-- TODO: Hide for add -->
        <t:parameter name="buttons">
            <input type="submit" name="delete" value="message:button.delete">
            <input type="submit" name="cancel" value="message:button.cancel">
        </t:parameter>
    </t:beaneditform>

    <script type="text/javascript">
        Form.focusFirstElement($("${pojoNameLower}Form"));
    </script>

</t:layout>