<%-- Error Messages --%>
<c:if test="${not empty error}">
    <div class="error">	
        <c:forEach var="err" items="${error}">
            <img src="<c:url value="/images/iconWarning.gif"/>"
                alt="<fmt:message key="icon.warning"/>" class="icon" />
            <c:out value="${err}" escapeXml="false"/><br />
        </c:forEach>
    </div>
</c:if>

<%-- Success Messages --%>
<c:if test="${not empty message}">
    <div class="message">	
        <c:forEach var="msg" items="${message}">
            <img src="<c:url value="/images/iconInformation.gif"/>"
                alt="<fmt:message key="icon.information"/>" class="icon" />
            <c:out value="${msg}" escapeXml="false"/><br />
        </c:forEach>
    </div>
</c:if>