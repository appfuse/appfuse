<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="activeUsers.title"/></title>
<content tag="heading"><fmt:message key="activeUsers.heading"/></content>
<body id="activeUsers"/>

<fmt:message key="activeUsers.message"/>

<div class="separator"></div>

<button type="button" onclick="location.href='mainMenu.html'">
    <fmt:message key="button.cancel"/>
</button>
    
<display:table name="applicationScope.userNames" id="user" cellspacing="0" cellpadding="0"
    defaultsort="1" styleClass="list activeUserList" pagesize="50" requestURI="">
  
    <%-- Table columns --%>
    <display:column property="username" width="30%" headerClass="sortable"
        titleKey="user.username" sort="true"/>
    <display:column property="fullName" headerClass="sortable"
        titleKey="activeUsers.fullName" sort="true"/>
        
    <display:setProperty name="paging.banner.item_name" value="user" />
    <display:setProperty name="paging.banner.items_name" value="users" />
</display:table>
