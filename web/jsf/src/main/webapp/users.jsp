<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="userList.title"/></title>
    <content tag="heading"><fmt:message key="userList.heading"/></content>
    <meta name="menu" content="AdminMenu"/>
</head>

<f:view>
<f:loadBundle var="text" basename="#{userList.bundleName}"/>

<h:form id="editUser">

<c:set var="buttons">
    <h:commandButton value="#{text['button.add']}" action="add" id="add" 
        immediate="true" styleClass="button"/>

    <input type="button" class="button" onclick="location.href='<c:url value="/mainMenu.html" />'"
        value="<fmt:message key="button.done"/>"/>
</c:set>

<c:out value="${buttons}" escapeXml="false" />

<%-- Use a non-displayed dataTable to pull userList into request --%>
<h:dataTable var="user" value="#{userList.users}" style="display:none"/>
    
<display:table name="userList.users" cellspacing="0" cellpadding="0" class="table"
    requestURI="/users.html" id="users" export="true" defaultsort="1" pagesize="25">
    <display:column sortable="true" titleKey="user.username" media="html" style="width: 25%">
        <a href="javascript:viewUser('<c:out value="${users.username}"/>')"><c:out value="${users.username}"/></a>
    </display:column>
    <display:column property="username" escapeXml="true" media="csv excel xml pdf" titleKey="user.username"/>
    <display:column property="fullName" escapeXml="true" sortable="true" titleKey="activeUsers.fullName" style="width: 34%"/>
    <display:column property="email" sortable="true" titleKey="user.email" style="width: 25%" autolink="true" media="html"/>
    <display:column property="email" titleKey="user.email" media="csv xml excel pdf"/>
    <display:column sortProperty="enabled" sortable="true" titleKey="user.enabled" style="width: 16%; padding-left: 15px" media="html">
        <input type="checkbox" disabled="disabled" <c:if test="${users.enabled}">checked="checked"</c:if>/>
    </display:column>
    <display:column property="enabled" titleKey="user.enabled" media="csv xml excel pdf"/>

    <display:setProperty name="paging.banner.item_name" value="user"/>
    <display:setProperty name="paging.banner.items_name" value="users"/>

    <display:setProperty name="export.excel.filename" value="User List.xls"/>
    <display:setProperty name="export.csv.filename" value="User List.csv"/>
    <display:setProperty name="export.pdf.filename" value="User List.pdf"/>
</display:table>

<c:out value="${buttons}" escapeXml="false" />

<%-- JSF Hack for the Display Tag, from James Violette --%>
<%-- 1. Create a dummy actionLink, w/ no value         --%>
<h:commandLink action="#{userForm.edit}" id="editUserLink">
    <f:param name="username" value=""/>
    <f:param name="from" value=""/>
</h:commandLink>
<%-- 2. Write your own JavaScript function that's easy to call --%>
<script type="text/javascript">
    function viewUser(username) {
        clear_editUser();
        var f = document.forms['editUser'];
        f.elements['editUser:_link_hidden_'].value='editUser:editUserLink';
        f.elements['username'].value=username;
        f.elements['from'].value='list';
        f.submit();
    }
    highlightTableRows("users");
</script>
</h:form>

</f:view> 
