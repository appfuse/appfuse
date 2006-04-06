<%@ include file="/common/taglibs.jsp"%>

<f:view>
<f:loadBundle var="text" basename="#{userList.bundleName}"/>

<title><fmt:message key="userList.title"/></title>
<content tag="heading"><fmt:message key="userList.heading"/></content>

<h:form id="editUser">

<c:set var="buttons">
    <h:commandButton value="#{text['button.add']}" action="add" id="add" 
        immediate="true" styleClass="button"/>

    <button type="button" onclick="location.href='<c:url value="/mainMenu.html" />'">
        <fmt:message key="button.done"/>
    </button>
</c:set>

<c:out value="${buttons}" escapeXml="false" />

<%-- Use a non-displayed dataTable to pull userList into request --%>
<h:dataTable var="user" value="#{userList.users}" style="display:none"/>
    
<display:table name="userList.users" cellspacing="0" cellpadding="0" class="list userList" 
    requestURI="/users.html" id="users" export="true" defaultsort="1" pagesize="25">
    <display:column sortable="true" titleKey="user.username" media="html" style="width: 17%">
        <a href="javascript:viewUser('<c:out value="${users.username}"/>')"><c:out value="${users.username}"/></a>
    </display:column>
    <display:column property="username" escapeXml="true" media="csv excel xml pdf" titleKey="user.username"/>
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

<%-- JSF Hack for the Display Tag, from James Violette --%>
<%-- 1. Create a dummy actionLink, w/ no value         --%>
<h:commandLink action="#{userForm.edit}" id="editUserLink">
    <f:param name="username"/>
    <f:param name="from"/>
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
