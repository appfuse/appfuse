<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="userList.title"/></title>
    <content tag="heading"><fmt:message key="userList.heading"/></content>
    <meta name="menu" content="AdminMenu"/>
</head>

<c:set var="buttons">
    <input type="button" style="margin-right: 5px"
        onclick="location.href='<html:rewrite forward="addUser"/>'"
        value="<fmt:message key="button.add"/>"/>
    
    <input type="button" onclick="location.href='<html:rewrite forward="mainMenu" />'"
        value="<fmt:message key="button.done"/>"/>
</c:set>

<c:out value="${buttons}" escapeXml="false"/>

<display:table name="userList" cellspacing="0" cellpadding="0" requestURI="" 
    defaultsort="1" id="users" pagesize="25" class="table" export="true">

    <display:column property="username" escapeXml="true" sortable="true" titleKey="userForm.username" style="width: 20%"
        url="/editUser.html?from=list" paramId="username" paramProperty="username"/>

    <display:column sortProperty="firstName" escapeXml="true" sortable="true" titleKey="activeUsers.fullName" style="width: 39%">
        <c:out value="${users.firstName} ${users.lastName}"/>
    </display:column>
    <display:column property="email" sortable="true" titleKey="userForm.email" style="width: 25%" autolink="true"/>
    <display:column sortProperty="enabled" sortable="true" titleKey="userForm.enabled" style="width: 16%; padding-left: 15px">
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
