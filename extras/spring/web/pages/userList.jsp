<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="userList.title"/></title>
<content tag="heading"><fmt:message key="userList.heading"/></content>

<c:set var="buttons">
    <button type="button" style="margin-right: 5px"
        onclick="location.href='<c:url value="/editUser.html"/>?method=Add&from=list'">
        <fmt:message key="button.add"/>
    </button>

    <button type="button" onclick="location.href='<c:url value="/mainMenu.html" />'">
        <fmt:message key="button.done"/>
    </button>
</c:set>

<c:out value="${buttons}" escapeXml="false" />

<display:table name="userList" cellspacing="0" cellpadding="0" requestURI="" 
    defaultsort="1" id="users" pagesize="25" class="list userList" export="true">

    <display:column property="username" escapeXml="true" sortable="true" titleKey="user.username" style="width: 17%"
        url="/editUser.html?from=list" paramId="username" paramProperty="username"/>

    <display:column property="firstName" escapeXml="true" sortable="true" titleKey="user.firstName" style="width: 20%"/>
    <display:column property="lastName" escapeXml="true" sortable="true" titleKey="user.lastName" style="width: 23%"/>
    <display:column property="email" sortable="true" titleKey="user.email" style="width: 25%" autolink="true"/>
    <display:column sortProperty="enabled" sortable="true" titleKey="user.enabled" style="width: 10%; padding-left: 15px">
        <input type="checkbox" disabled="disabled" <c:if test="${users.enabled}">checked="checked"</c:if>/>
    </display:column> 

    <display:setProperty name="paging.banner.item_name" value="user"/>
    <display:setProperty name="paging.banner.items_name" value="users"/>

    <display:setProperty name="export.excel.filename" value="User List.xls"/>
    <display:setProperty name="export.csv.filename" value="User List.csv"/>
    <display:setProperty name="export.pdf.filename" value="User List.pdf"/>
</display:table>

<c:out value="${buttons}" escapeXml="false" />

<script type="text/javascript">
highlightTableRows("users");
</script>
