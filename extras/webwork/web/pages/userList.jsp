<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="userList.title"/></title>
<content tag="heading"><fmt:message key="userList.heading"/></content>

<ww:set name="userList" value="users" scope="request"/>

<c:set var="buttons">
    <input type="button" style="margin-right: 5px"
        onclick="location.href='<c:url value="/editUser.html?method=Add&from=list"/>'"
        value="<fmt:message key="button.add"/>"/>
    
    <input type="button" onclick="location.href='<c:url value="/mainMenu.html"/>'"
        value="<fmt:message key="button.done"/>"/>
</c:set>

<c:out value="${buttons}" escapeXml="false" />

<c:if test="${not empty requestScope.userList}">
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
</c:if>
<c:if test="${empty requestScope.userList}">
    <fmt:message key="userList.nousers"/>
</c:if>

<c:out value="${buttons}" escapeXml="false" />

<script type="text/javascript">
    highlightTableRows("users");
</script>
