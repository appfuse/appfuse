<%@ include file="/common/taglibs.jsp"%>


<p><fmt:message key="welcome.message"/></p>

<%-- Include the login form --%>
<jsp:include page="/WEB-INF/pages/loginForm.jsp" flush="true"/>

<p><fmt:message key="login.passwordHint"/></p>

<p><a href="#" 
    onclick="toggleDisplay('readme'); return false">Toggle README</a></p>
    
<div id="readme" style="display: none">
    <%-- Include the README.txt --%>
    <pre>
    <%@ include file="/README.txt"%>
    </pre>
</div>

