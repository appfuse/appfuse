<%@ include file="/common/taglibs.jsp"%>

<c:set var="buttons">
    <button type="button" name="action" style="margin-right: 5px"
        onclick="location.href='<c:url value="/editUser.html"/>?method=Add&from=list'">
        <fmt:message key="button.add"/>
    </button>
    
    <button type="button" name="action" onclick="location.href='<c:url value="/mainMenu.html" />'">
        <fmt:message key="button.cancel"/>
    </button>
</c:set>

<c:out value="${buttons}" escapeXml="false" />

<c:if test="${not empty requestScope.userList}">
<display:table name="${userList}" cellspacing="0" cellpadding="0"
    requestURI="" defaultsort="1"
    pagesize="25" styleClass="list userList" export="true">
  
    <%-- Table columns --%>
    <display:column property="username" sort="true" 
    	headerClass="sortable" width="17%"
        url="/editUser.html?from=list" 
        paramId="username" paramProperty="username"
        titleKey="user.username"/>
    <display:column property="firstName" sort="true" 
    	headerClass="sortable" width="20%"
        titleKey="user.firstName" />
    <display:column property="lastName" sort="true" 
    	headerClass="sortable" width="13%"
        titleKey="user.lastName"/>
    <display:column property="email" sort="true" headerClass="sortable" 
    	width="26%" autolink="true"
        titleKey="user.email" />
        
    <display:setProperty name="paging.banner.item_name" value="user"/>
    <display:setProperty name="paging.banner.items_name" value="users"/>

    <display:setProperty name="export.excel.filename" value="User List.xls"/>
    <display:setProperty name="export.csv.filename" value="User List.csv"/>
</display:table>
</c:if>
<c:if test="${empty requestScope.userList}">
	<fmt:message key="userList.nousers"/>
</c:if>

<c:out value="${buttons}" escapeXml="false" />
            
<script type="text/javascript">
<!--
    var previousClass = null;
    var table = document.getElementsByTagName("table")[0];    
    var tbody = table.getElementsByTagName("tbody")[0];
    var rows = tbody.getElementsByTagName("tr");
    // add event handlers so rows light up and are clickable
    for (i=0; i < rows.length; i++) {
        rows[i].onmouseover = function() { previousClass=this.className;this.className+=' over' };
        rows[i].onmouseout = function() { this.className=previousClass };
        rows[i].onclick = function() {
            var cell = this.getElementsByTagName("td")[0];
            var link = cell.getElementsByTagName("a")[0];
            location.href = link.getAttribute("href");
            this.style.cursor="wait";
        }
    }
//-->
</script>
