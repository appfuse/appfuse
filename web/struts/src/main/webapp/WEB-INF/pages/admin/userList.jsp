<%@ include file="/common/taglibs.jsp" %>

<head>
    <title><fmt:message key="userList.title"/></title>
    <meta name="heading" content="<fmt:message key='userList.heading'/>"/>
    <meta name="menu" content="AdminMenu"/>
</head>

<div class="span10">
    <h2><fmt:message key="userList.heading"/></h2>

    <div id="search">
        <form method="get" action="${ctx}/restaurants" id="searchForm" class="form-search">
            <input type="text" size="20" name="q" id="query" value="${param.q}"
                   placeholder="<fmt:message key="search.enterTerms"/>" class="input-medium search-query"/>
            <button id="button.search" class="btn">
                <i class="icon-search"></i>
                <fmt:message key="button.search"/>
            </button>
        </form>
    </div>

    <div id="actions" class="form-actions">
        <a class="btn btn-primary" href="<c:url value='/editUser?method=Add&from=list'/>" >
            <i class="icon-plus"></i>
            <fmt:message key="button.add"/>
        </a>
        <a class="btn" href="<c:url value="/mainMenu"/>" >
            <fmt:message key="button.done"/>
        </a>
    </div>

    <display:table name="users" cellspacing="0" cellpadding="0" requestURI=""
                   defaultsort="1" id="users" pagesize="25" class="table table-condensed" export="true">
        <display:column property="username" escapeXml="true" sortable="true" titleKey="user.username" style="width: 25%"
                        url="/editUser?from=list" paramId="id" paramProperty="id"/>
        <display:column property="fullName" escapeXml="true" sortable="true" titleKey="activeUsers.fullName"
                        style="width: 34%"/>
        <display:column property="email" sortable="true" titleKey="user.email" style="width: 25%" autolink="true"
                        media="html"/>
        <display:column property="email" titleKey="user.email" media="csv xml excel pdf"/>
        <display:column sortProperty="enabled" sortable="true" titleKey="user.enabled"
                        style="width: 16%; padding-left: 15px" media="html">
            <input type="checkbox" disabled="disabled" <c:if test="${users.enabled}">checked="checked"</c:if>/>
        </display:column>
        <display:column property="enabled" titleKey="user.enabled" media="csv xml excel pdf"/>

        <display:setProperty name="paging.banner.item_name"><fmt:message key="userList.user"/></display:setProperty>
        <display:setProperty name="paging.banner.items_name"><fmt:message key="userList.users"/></display:setProperty>

        <display:setProperty name="export.excel.filename" value="User List.xls"/>
        <display:setProperty name="export.csv.filename" value="User List.csv"/>
        <display:setProperty name="export.pdf.filename" value="User List.pdf"/>
    </display:table>

    <div id="actions" class="form-actions">
        <a class="btn btn-primary" href="<c:url value='/editUser?method=Add&from=list'/>" >
            <i class="icon-plus"></i>
            <fmt:message key="button.add"/>
        </a>
        <a class="btn" href="<c:url value="/mainMenu"/>" >
            <fmt:message key="button.done"/>
        </a>
    </div>
</div>
