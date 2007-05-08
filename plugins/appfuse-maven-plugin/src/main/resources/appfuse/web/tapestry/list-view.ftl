<head>
	<title><span key="${pojo.shortName.toLowerCase()}List.title"/></title>
	<content tag="heading"><span key="${pojo.shortName.toLowerCase()}List.heading"/></content>
	<meta name="menu" content="${pojo.shortName}Menu"/>
</head>

<span jwcid="@ShowMessage"/>

<p>
    <input type="button" class="button" onclick="location.href='${pojo.shortName}Form.html'" jwcid="@Any" value="message:button.add"/>
    <input type="button" class="button" onclick="location.href='mainMenu.html'" jwcid="@Any" value="message:button.done"/>
</p>

<table jwcid="table@contrib:Table" class="table contribTable ${pojo.shortName.toLowerCase()}List" id="${pojo.shortName.toLowerCase()}List"
    rowsClass="ognl:beans.rowsClass.next" row="ognl:row" source="ognl:${pojo.shortName.toLowerCase()}s"
    columns="${pojo.shortName.toLowerCase()}.id:id,${pojo.shortName.toLowerCase()}.firstName:firstName,${pojo.shortName.toLowerCase()}.lastName:lastName"
    arrowUpAsset="asset:upArrow" arrowDownAsset="asset:downArrow">
    <tr jwcid="${pojo.shortName.toLowerCase()}_idColumnValue@Block">
        <a jwcid="@DirectLink" listener="listener:edit" parameters="ognl:row.id">
            <span jwcid="@Insert" value="ognl:row.id"/>
        </a>
    </tr>
</table>

<input type="button" class="button" onclick="location.href='${pojo.shortName}Form.html'" jwcid="@Any" value="message:button.add"/>
<input type="button" class="button" onclick="location.href='mainMenu.html'" jwcid="@Any" value="message:button.done"/>

<script type="text/javascript">
    highlightTableRows("${pojo.shortName.toLowerCase()}List");
</script>
