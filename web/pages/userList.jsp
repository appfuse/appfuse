<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="userList.title"/></title>
<content tag="heading"><fmt:message key="userList.heading"/></content>

<%-- For linking to edit screen --%>
<bean:struts id="editURL" forward="editUser"/>

<c:set var="buttons">
    <button type="button" style="margin-right: 5px"
        onclick="location.href='<html:rewrite forward="addUser"/>'">
        <fmt:message key="button.add"/>
    </button>
    
    <button type="button" onclick="location.href='<html:rewrite forward="mainMenu" />'">
        <fmt:message key="button.cancel"/>
    </button>
</c:set>

<c:out value="${buttons}" escapeXml="false" />

<display:table name="${userList}" cellspacing="0" cellpadding="0"
    requestURI="" defaultsort="1" export="true" id="users"
    pagesize="25" styleClass="list userList">
  
    <%-- Table columns --%>
    <display:column property="username" sort="true" 
    	headerClass="sortable" width="17%"
        url="${editURL.path}?from=list" 
        paramId="username" paramProperty="username"
        titleKey="userForm.username"/>
    <display:column property="firstName" sort="true" 
    	headerClass="sortable" width="20%"
        titleKey="userForm.firstName" />
    <display:column property="lastName" sort="true" 
    	headerClass="sortable" width="13%"
        titleKey="userForm.lastName"/>
    <display:column property="email" sort="true" headerClass="sortable" 
    	width="26%" autolink="true"
        titleKey="userForm.email" />
        
    <display:setProperty name="paging.banner.item_name" value="user"/>
    <display:setProperty name="paging.banner.items_name" value="users"/>

    <display:setProperty name="export.excel.filename" value="User List.xls"/>
    <display:setProperty name="export.csv.filename" value="User List.csv"/>
</display:table>

<c:out value="${buttons}" escapeXml="false" />
            
<script type="text/javascript">
<!--
highlightTableRows("users");
//-->
</script>
