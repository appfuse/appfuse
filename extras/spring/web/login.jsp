<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="login.title"/></title>
<content tag="heading"><fmt:message key="login.heading"/></content>
<body id="login"/>

<c:import url="/loginMenu.jsp"/>

<p><fmt:message key="welcome.message"/></p>

<%-- Include the login form --%>
<jsp:include page="/WEB-INF/pages/loginForm.jsp" flush="true"/>

<p><fmt:message key="login.passwordHint"/></p>

<p><a href="#" 
    onclick="toggleDisplay('readme'); return false">Toggle README</a></p>
    
<div id="readme" style="display: none">
    <%-- Include the README.txt --%>
    <pre><jsp:include page="/README.txt"/></pre>
</div>
