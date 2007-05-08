<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="${pojo.shortName.toLowerCase()}List.title"/></title>
<content tag="heading"><fmt:message key="${pojo.shortName.toLowerCase()}List.heading"/></content>
<meta name="menu" content="${pojo.shortName}Menu"/>

<c:set var="buttons">
    <input type="button" style="margin-right: 5px"
        onclick="location.href='<c:url value="/${pojo.shortName.toLowerCase()}form.html"/>'"
        value="<fmt:message key="button.add"/>"/>

    <input type="button" onclick="location.href='<c:url value="/mainMenu.html"/>'"
        value="<fmt:message key="button.done"/>"/>
</c:set>

<c:out value="${'$'}{buttons}" escapeXml="false"/>

<display:table name="${pojo.shortName.toLowerCase()}List" cellspacing="0" cellpadding="0" requestURI=""
    id="${pojo.shortName.toLowerCase()}List" pagesize="25" class="table ${pojo.shortName.toLowerCase()}List" export="true">

    <display:column property="id" escapeXml="true" sortable="true"
        url="/${pojo.shortName.toLowerCase()}form.html" paramId="id" paramProperty="id" titleKey="${pojo.shortName.toLowerCase()}.id"/>
    <display:column property="firstName" escapeXml="true" sortable="true" titleKey="${pojo.shortName.toLowerCase()}.firstName"/>
    <display:column property="lastName" escapeXml="true" sortable="true" titleKey="${pojo.shortName.toLowerCase()}.lastName"/>

    <display:setProperty name="paging.banner.item_name" value="${pojo.shortName.toLowerCase()}"/>
    <display:setProperty name="paging.banner.items_name" value="people"/>

    <display:setProperty name="export.excel.filename" value="${pojo.shortName} List.xls"/>
    <display:setProperty name="export.csv.filename" value="${pojo.shortName} List.csv"/>
    <display:setProperty name="export.pdf.filename" value="${pojo.shortName} List.pdf"/>
</display:table>

<c:out value="${'$'}{buttons}" escapeXml="false"/>

<script type="text/javascript">
    highlightTableRows("${pojo.shortName.toLowerCase()}List");
</script> 