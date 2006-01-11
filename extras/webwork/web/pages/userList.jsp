<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="userList.title"/></title>
<content tag="heading"><fmt:message key="userList.heading"/></content>

<ww:set name="userList" value="users" scope="request"/>

<c:set var="buttons">
    <button type="button" style="margin-right: 5px"
        onclick="location.href='<c:url value="/editUser.html"/>?method=Add&from=list'">
        <fmt:message key="button.add"/>
    </button>
    
    <button type="button" onclick="location.href='<c:url value="/mainMenu.html" />'">
        <fmt:message key="button.cancel"/>
    </button>
</c:set>

<c:out value="${buttons}" escapeXml="false" />

<c:if test="${not empty requestScope.userList}">
<display:table name="userList" cellspacing="0" cellpadding="0"
    requestURI="" defaultsort="1" id="users" pagesize="25" 
    styleClass="list userList" export="true">

    <display:column property="username" sort="true" titleKey="user.username" width="17%"
        url="/editUser.html?from=list" paramId="username" paramProperty="username"/>

    <display:column property="firstName" sort="true" titleKey="user.firstName" width="20%"/>
    <display:column property="lastName" sort="true" titleKey="user.lastName" width="23%"/>
    <display:column property="email" sort="true" titleKey="user.email" width="40%" autolink="true"/>
    <display:column property="enabled" sort="true" titleKey="user.enabled" width="10%"/>

    <display:setProperty name="paging.banner.item_name" value="user"/>
    <display:setProperty name="paging.banner.items_name" value="users"/>

    <display:setProperty name="export.excel.filename" value="User List.xls"/>
    <display:setProperty name="export.csv.filename" value="User List.csv"/>
    <display:setProperty name="export.pdf.filename" value="User List.pdf"/>
</display:table>
</c:if>
<c:if test="${empty requestScope.userList}">
    <fmt:message key="userList.nousers"/>
</c:if>

<c:out value="${buttons}" escapeXml="false" />

<script type="text/javascript">
highlightTableRows("users");
</script>
