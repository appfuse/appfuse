<#assign pojoNameLower = pojo.shortName.substring(0,1).toLowerCase()+pojo.shortName.substring(1)>
<t:layout title="message:${pojoNameLower}List.title"
          heading="message:${pojoNameLower}List.heading" menu="literal:${pojo.shortName}Menu"
          xmlns:t="http://tapestry.apache.org/schema/tapestry_5_0_0.xsd">

    <t:messagebanner id="message" type="type"/>
    <p>
        <t:eventlink id="addTop">
            <input type="button" class="button" value="$\{message:button.add}"/>
        </t:eventlink>
        <t:eventlink id="doneTop">
            <input type="button" class="button" value="$\{message:button.done}"/>
        </t:eventlink>
    </p>

    <t:grid source="${util.getPluralForWord(pojoNameLower)}" row="${pojoNameLower}" id="${pojoNameLower}List" class="table">
        <t:parameter name="${pojo.identifierProperty.name}Cell">
            <t:pagelink page="${pojoNameLower}form" context="${pojoNameLower}.${pojo.identifierProperty.name}">
                ${'$'}{${pojoNameLower}.${pojo.identifierProperty.name}}
            </t:pagelink>
        </t:parameter>
    </t:grid>

    <p>
        <t:eventlink id="addBottom">
            <input type="button" class="button" value="$\{message:button.add}"/>
        </t:eventlink>
        <t:eventlink id="doneBottom">
            <input type="button" class="button" value="$\{message:button.done}"/>
        </t:eventlink>
    </p>

    <script type="text/javascript">
        highlightTableRows("${pojoNameLower}List");
    </script>
</t:layout>